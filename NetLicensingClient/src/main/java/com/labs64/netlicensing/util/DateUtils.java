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
package com.labs64.netlicensing.util;

import java.util.Calendar;
import java.util.TimeZone;

import javax.xml.bind.DatatypeConverter;

public class DateUtils {

    public static Calendar getCurrentDate() {
        return Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    }

    public static Calendar parseDate(final String dateTime) {
        Calendar dateTimeCl = DatatypeConverter.parseDateTime(dateTime);
        dateTimeCl.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateTimeCl;
    }

    public static String printDate(final Calendar date) {
        date.setTimeZone(TimeZone.getTimeZone("UTC"));
        return DatatypeConverter.printDateTime(date);
    }

}
