/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.labs64.netlicensing.utils;

import static java.lang.System.out;

import com.labs64.netlicensing.domain.vo.Page;

/**
 * Utility class for writing to console
 */
public class ConsoleWriter {

    public void writeMessage(final String msg) {
        out.println(msg);
        out.println();
    }

    public void writeException(final String msg, final Exception ex) {
        out.println(msg);
        ex.printStackTrace();
        out.println();
    }

    public void writeObject(final String msg, final Object obj) {
        out.println(msg);
        out.println(obj);
        out.println();
    }

    public void writePage(final String msg, final Page<?> page) {
        out.println(msg);
        if (page != null && page.hasContent()) {
            for (final Object object : page.getContent()) {
                out.println(object);
            }
        }
        out.println();
    }

}
