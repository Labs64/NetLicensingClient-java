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
package com.labs64.netlicensing.domain.vo;

public enum LicenseType {

    /**
     * licenseType: feature.
     */
    FEATURE("FEATURE"),

    /**
     * licenseType: TimeVolume.
     */
    TIMEVOLUME("TIMEVOLUME"),

    /**
     * licenseType: TimeVolume.
     */
    FLOATING("FLOATING"),

    /**
     * licenseType: Quantity.
     */
    QUANTITY("QUANTITY");

    private final String value;

    /**
     * Instantiates a new license type.
     *
     * @param licenseTypeValue
     *            licenseType value
     */
    LicenseType(final String licenseTypeValue) {
        value = licenseTypeValue;
    }

    /**
     * Get enum value.
     *
     * @return enum value
     */
    public String value() {
        return value;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Parse license type value to {@link LicenseType} enum.
     *
     * @param value
     *            licenseType value
     * @return {@link LicenseType} enum object or throws {@link IllegalArgumentException} if no corresponding
     *         {@link LicenseType} enum object found
     */
    public static LicenseType parseValue(final String value) {
        for (final LicenseType licenseType : LicenseType.values()) {
            if (licenseType.value.equalsIgnoreCase(value)) {
                return licenseType;
            }
        }
        throw new IllegalArgumentException(value);
    }

    /**
     * Parse license type value to {@link LicenseType} enum, nothrow version.
     *
     * @param value
     *            licenseType value as string
     * @return {@link LicenseType} enum object or {@code null} if argument doesn't match any of the enum values
     */
    public static LicenseType parseValueSafe(final String value) {
        try {
            return parseValue(value);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }

}
