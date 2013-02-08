/*
 * Copyright 2013 JBoss Inc
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

package org.drools.planner.core.score.buildin.simpledouble;

import org.drools.planner.core.score.buildin.AbstractScoreTest;
import org.junit.Test;

public class SimpleDoubleScoreTest extends AbstractScoreTest {

    @Test
    public void equalsAndHashCode() {
        assertScoresEqualsAndHashCode(
                SimpleDoubleScore.valueOf(-10.0),
                SimpleDoubleScore.valueOf(-10.0)
        );
    }

    @Test
    public void compareTo() {
        assertScoreCompareToOrder(
                SimpleDoubleScore.valueOf(-300.5),
                SimpleDoubleScore.valueOf(-300.0),
                SimpleDoubleScore.valueOf(-20.06),
                SimpleDoubleScore.valueOf(-20.007),
                SimpleDoubleScore.valueOf(-20.0),
                SimpleDoubleScore.valueOf(-1.0),
                SimpleDoubleScore.valueOf(0.0),
                SimpleDoubleScore.valueOf(1.0)
        );
    }

}
