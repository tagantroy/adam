/*
 * Copyright (C) 2019 Anton Malinskiy
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

package com.malinskiy.adam.request.sync

import com.malinskiy.adam.Const
import com.malinskiy.adam.request.ComplexRequest
import com.malinskiy.adam.request.HostTarget
import com.malinskiy.adam.transport.AndroidReadChannel
import com.malinskiy.adam.transport.AndroidWriteChannel

/**
 * @see https://android.googlesource.com/platform/system/core/+/refs/heads/master/adb/adb.h#62
 */
class GetAdbServerVersionRequest : ComplexRequest<Int>(target = HostTarget) {
    override suspend fun readElement(readChannel: AndroidReadChannel, writeChannel: AndroidWriteChannel): Int {
        val sizeBuffer = ByteArray(4)
        readChannel.readFully(sizeBuffer, 0, 4)

        val length = String(sizeBuffer, Const.DEFAULT_TRANSPORT_ENCODING).toInt(radix = 16)
        val versionBuffer = ByteArray(length)
        readChannel.readFully(versionBuffer, 0, length)

        return String(versionBuffer, Const.DEFAULT_TRANSPORT_ENCODING).toInt(radix = 16)
    }

    override fun serialize() = createBaseRequest("version")
}