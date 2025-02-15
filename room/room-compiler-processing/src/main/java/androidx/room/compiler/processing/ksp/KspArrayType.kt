/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.room.compiler.processing.ksp

import androidx.room.compiler.codegen.JArrayTypeName
import androidx.room.compiler.processing.XArrayType
import androidx.room.compiler.processing.XNullability
import androidx.room.compiler.processing.XType
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Variance
import com.squareup.kotlinpoet.javapoet.JTypeName
import com.squareup.kotlinpoet.javapoet.KTypeName

internal sealed class KspArrayType(
    env: KspProcessingEnv,
    ksType: KSType,
    originalKSAnnotations: Sequence<KSAnnotation>,
    scope: KSTypeVarianceResolverScope? = null,
    typeAlias: KSType? = null,
) : KspType(env, ksType, originalKSAnnotations, scope, typeAlias), XArrayType {

    abstract override val componentType: KspType

    override fun resolveJTypeName(): JTypeName {
        return this.asTypeName().java
    }

    override fun resolveKTypeName(): KTypeName {
        return this.asTypeName().kotlin
    }

    override fun boxed() = this

    override val typeArguments: List<XType>
        get() = emptyList() // hide them to behave like java does

    /** Kotlin arrays in the form of Array<X>. */
    private class BoxedArray(
        env: KspProcessingEnv,
        ksType: KSType,
        originalKSAnnotations: Sequence<KSAnnotation> = ksType.annotations,
        scope: KSTypeVarianceResolverScope? = null,
        typeAlias: KSType? = null,
    ) : KspArrayType(env, ksType, originalKSAnnotations, scope, typeAlias) {
        override fun resolveJTypeName(): JTypeName {
            return if (ksType.arguments.single().variance == Variance.CONTRAVARIANT) {
                JArrayTypeName.of(JTypeName.OBJECT)
            } else {
                JArrayTypeName.of(componentType.asTypeName().java.box())
            }
        }

        override fun resolveKTypeName(): KTypeName {
            return ksType.asKTypeName(env.resolver)
        }

        override val componentType: KspType by lazy {
            val arg = ksType.arguments.single()
            // https://kotlinlang.org/docs/reference/basic-types.html#primitive-type-arrays
            // these are always boxed
            env.wrap(ksType = checkNotNull(arg.type?.resolve()), allowPrimitives = false)
        }

        override fun copy(
            env: KspProcessingEnv,
            ksType: KSType,
            originalKSAnnotations: Sequence<KSAnnotation>,
            scope: KSTypeVarianceResolverScope?,
            typeAlias: KSType?
        ) = BoxedArray(env, ksType, originalKSAnnotations, scope, typeAlias)
    }

    /** Built in primitive array types (e.g. IntArray) */
    private class PrimitiveArray(
        env: KspProcessingEnv,
        ksType: KSType,
        originalKSAnnotations: Sequence<KSAnnotation> = ksType.annotations,
        scope: KSTypeVarianceResolverScope? = null,
        typeAlias: KSType? = null,
        override val componentType: KspType,
    ) : KspArrayType(env, ksType, originalKSAnnotations, scope, typeAlias) {
        override fun resolveJTypeName(): JTypeName {
            return JArrayTypeName.of(componentType.asTypeName().java.unbox())
        }

        override fun resolveKTypeName(): KTypeName {
            return ksType.asKTypeName(env.resolver)
        }

        override fun copy(
            env: KspProcessingEnv,
            ksType: KSType,
            originalKSAnnotations: Sequence<KSAnnotation>,
            scope: KSTypeVarianceResolverScope?,
            typeAlias: KSType?
        ) = PrimitiveArray(env, ksType, originalKSAnnotations, scope, typeAlias, componentType)
    }

    /** Factory class to create instances of [KspArrayType]. */
    internal class Factory(private val env: KspProcessingEnv) {
        // map of built in array type to its component type
        private val builtInArrays =
            mapOf(
                "kotlin.BooleanArray" to KspPrimitiveType(env, env.resolver.builtIns.booleanType),
                "kotlin.ByteArray" to KspPrimitiveType(env, env.resolver.builtIns.byteType),
                "kotlin.CharArray" to KspPrimitiveType(env, env.resolver.builtIns.charType),
                "kotlin.DoubleArray" to KspPrimitiveType(env, env.resolver.builtIns.doubleType),
                "kotlin.FloatArray" to KspPrimitiveType(env, env.resolver.builtIns.floatType),
                "kotlin.IntArray" to KspPrimitiveType(env, env.resolver.builtIns.intType),
                "kotlin.LongArray" to KspPrimitiveType(env, env.resolver.builtIns.longType),
                "kotlin.ShortArray" to KspPrimitiveType(env, env.resolver.builtIns.shortType),
            )

        // map from the primitive to its array
        private val reverseBuiltInArrayLookup =
            builtInArrays.entries.associateBy { it.value.ksType }

        fun createWithComponentType(componentType: KspType): KspArrayType {
            if (componentType.nullability == XNullability.NONNULL) {
                val primitiveArrayEntry: Map.Entry<String, KspPrimitiveType>? =
                    reverseBuiltInArrayLookup[componentType.ksType]
                if (primitiveArrayEntry != null) {
                    return PrimitiveArray(
                        env = env,
                        ksType = env.resolver.requireType(primitiveArrayEntry.key),
                        componentType = primitiveArrayEntry.value,
                    )
                }
            }

            return BoxedArray(
                env = env,
                ksType =
                    env.resolver.builtIns.arrayType.replace(
                        listOf(
                            env.resolver.getTypeArgument(
                                componentType.ksType.createTypeReference(),
                                if (componentType is KspTypeArgumentType) {
                                    componentType.typeArg.variance
                                } else {
                                    Variance.INVARIANT
                                }
                            )
                        )
                    ),
            )
        }

        /**
         * Creates and returns a [KspArrayType] if and only if the given [ksType] represents an
         * array.
         */
        fun createIfArray(ksType: KSType): KspArrayType? {
            val qName = ksType.declaration.qualifiedName?.asString()
            if (qName == KOTLIN_ARRAY_Q_NAME) {
                return BoxedArray(
                    env = env,
                    ksType = ksType,
                )
            }
            builtInArrays[qName]?.let { primitiveType ->
                return PrimitiveArray(
                    env = env,
                    ksType = ksType,
                    componentType = primitiveType,
                )
            }
            return null
        }
    }

    companion object {
        const val KOTLIN_ARRAY_Q_NAME = "kotlin.Array"
    }
}
