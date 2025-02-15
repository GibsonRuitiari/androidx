/*
 * Copyright (C) 2016 The Android Open Source Project
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

package androidx.room.solver.types

import androidx.room.compiler.codegen.XTypeName
import androidx.room.compiler.processing.XType
import androidx.room.parser.SQLTypeAffinity

/** A code generator that can read a field from Cursor and write a field to a Statement */
abstract class ColumnTypeAdapter(val out: XType, val typeAffinity: SQLTypeAffinity) :
    StatementValueBinder, StatementValueReader {
    val outTypeName: XTypeName by lazy { out.asTypeName() }

    override fun typeMirror() = out

    override fun affinity(): SQLTypeAffinity = typeAffinity
}
