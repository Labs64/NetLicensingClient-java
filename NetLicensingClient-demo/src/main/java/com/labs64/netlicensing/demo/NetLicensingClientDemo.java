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
package com.labs64.netlicensing.demo;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.labs64.netlicensing.examples.NetLicensingExample;

public class NetLicensingClientDemo {

    public static void main(final String[] args) {
        final Options options = new Options();
        options.addOption("l", false, "list available examples");
        options.addOption("r", true, "run specified example");

        final CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (final ParseException e) {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar netlicensing-client-demo.jar <option>", options);
            return;
        }

        if (cmd.hasOption("l")) {
            AllExamples.list.forEach((key, val) -> {
                System.out.println("Available examples:");
                System.out.println("  " + key);
            });
            return;
        }

        String exampleToRun = cmd.getOptionValue("r");
        if (exampleToRun == null) {
            exampleToRun = AllExamples.list.keySet().iterator().next();
        }
        if (AllExamples.list.containsKey(exampleToRun)) {
            NetLicensingExample ex;
            try {
                ex = AllExamples.list.get(exampleToRun).newInstance();
                ex.execute();
                return;
            } catch (final Exception e) {
                System.out.println("Error executing example '" + exampleToRun + "'");
                e.printStackTrace();
            }
        }
        System.out.println("Couldn't find example '" + exampleToRun + "'");
    }

}
