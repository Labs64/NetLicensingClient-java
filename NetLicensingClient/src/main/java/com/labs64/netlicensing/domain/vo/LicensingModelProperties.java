/* Licensed under the Apache License, Version 2.0 (the "License");
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
package com.labs64.netlicensing.domain.vo;

/**
 * Licensing models must implement this interface.
 * <p>
 * On events that require licensing model specific actions, corresponding event handlers are called. Event handlers that
 * do not return any values expected to validate the passed arguments, possibly modifying them. Must throw exceptions in
 * case validation is not passed.
 */
public interface LicensingModelProperties {

    /**
     * Gets the licensing model name.
     * 
     * @return the licensing model name
     */
    String getName();

}
