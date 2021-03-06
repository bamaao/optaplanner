/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.core.api.score.buildin.bendable;

import org.junit.Test;
import org.optaplanner.core.api.score.buildin.AbstractScoreTest;
import org.optaplanner.core.impl.score.buildin.bendable.BendableScoreDefinition;
import org.optaplanner.core.impl.testdata.util.PlannerAssert;
import org.optaplanner.core.impl.testdata.util.PlannerTestUtils;

import static org.junit.Assert.*;

public class BendableScoreTest extends AbstractScoreTest {

    private BendableScoreDefinition scoreDefinitionHSS = new BendableScoreDefinition(1, 2);
    private BendableScoreDefinition scoreDefinitionHHH = new BendableScoreDefinition(3, 0);
    private BendableScoreDefinition scoreDefinitionSSS = new BendableScoreDefinition(0, 3);

    @Test
    public void parseScore() {
        assertEquals(scoreDefinitionHSS.createScore(-147, -258, -369),
                scoreDefinitionHSS.parseScore("[-147]hard/[-258/-369]soft"));
        assertEquals(scoreDefinitionHHH.createScore(-147, -258, -369),
                scoreDefinitionHHH.parseScore("[-147/-258/-369]hard/[]soft"));
        assertEquals(scoreDefinitionSSS.createScore(-147, -258, -369),
                scoreDefinitionSSS.parseScore("[]hard/[-147/-258/-369]soft"));
        assertEquals(scoreDefinitionSSS.createScoreUninitialized(-7, -147, -258, -369),
                scoreDefinitionSSS.parseScore("-7init/[]hard/[-147/-258/-369]soft"));
    }

    @Test
    public void toShortString() {
        assertEquals("0", scoreDefinitionHSS.createScore(0, 0, 0).toShortString());
        assertEquals("[0/-369]soft", scoreDefinitionHSS.createScore(0, 0, -369).toShortString());
        assertEquals("[-258/-369]soft", scoreDefinitionHSS.createScore(0, -258, -369).toShortString());
        assertEquals("[-147]hard", scoreDefinitionHSS.createScore(-147, 0, 0).toShortString());
        assertEquals("[-147]hard/[-258/-369]soft", scoreDefinitionHSS.createScore(-147, -258, -369).toShortString());
        assertEquals("[-147/-258/-369]hard", scoreDefinitionHHH.createScore(-147, -258, -369).toShortString());
        assertEquals("[-147/-258/-369]soft", scoreDefinitionSSS.createScore(-147, -258, -369).toShortString());
        assertEquals("-7init/[-147/-258/-369]soft", scoreDefinitionSSS.createScoreUninitialized(-7, -147, -258, -369).toShortString());
    }

    @Test
    public void testToString() {
        assertEquals("[0]hard/[-258/-369]soft", scoreDefinitionHSS.createScore(0, -258, -369).toString());
        assertEquals("[-147]hard/[-258/-369]soft", scoreDefinitionHSS.createScore(-147, -258, -369).toString());
        assertEquals("[-147/-258/-369]hard/[]soft", scoreDefinitionHHH.createScore(-147, -258, -369).toString());
        assertEquals("[]hard/[-147/-258/-369]soft", scoreDefinitionSSS.createScore(-147, -258, -369).toString());
        assertEquals("-7init/[]hard/[-147/-258/-369]soft", scoreDefinitionSSS.createScoreUninitialized(-7, -147, -258, -369).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseScoreIllegalArgument() {
        scoreDefinitionHSS.parseScore("-147");
    }

    @Test
    public void getHardOrSoftScore() {
        BendableScore initializedScore = scoreDefinitionHSS.createScore(-5, -10, -200);
        assertEquals(-5, initializedScore.getHardOrSoftScore(0));
        assertEquals(-10, initializedScore.getHardOrSoftScore(1));
        assertEquals(-200, initializedScore.getHardOrSoftScore(2));
    }

    @Test
    public void toInitializedScoreHSS() {
        assertEquals(scoreDefinitionHSS.createScore(-147, -258, -369),
                scoreDefinitionHSS.createScore(-147, -258, -369).toInitializedScore());
        assertEquals(scoreDefinitionHSS.createScore(-147, -258, -369),
                scoreDefinitionHSS.createScoreUninitialized(-7, -147, -258, -369).toInitializedScore());
    }

    @Test
    public void withInitScore() {
        assertEquals(scoreDefinitionHSS.createScoreUninitialized(-7, -147, -258, -369),
                scoreDefinitionHSS.createScore(-147, -258, -369).withInitScore(-7));
    }

    @Test
    public void feasibleHSS() {
        assertScoreNotFeasible(
                scoreDefinitionHSS.createScore(-20, -300, -4000),
                scoreDefinitionHSS.createScoreUninitialized(-1, 20, -300, -4000),
                scoreDefinitionHSS.createScoreUninitialized(-1, 0, -300, -4000),
                scoreDefinitionHSS.createScoreUninitialized(-1, -20, -300, -4000)
        );
        assertScoreFeasible(
                scoreDefinitionHSS.createScore(0, -300, -4000),
                scoreDefinitionHSS.createScore(20, -300, -4000),
                scoreDefinitionHSS.createScoreUninitialized(0, 0, -300, -4000)
                );
    }

    @Test
    public void addHSS() {
        assertEquals(scoreDefinitionHSS.createScore(19, -320, 0),
                scoreDefinitionHSS.createScore(20, -20, -4000).add(
                        scoreDefinitionHSS.createScore(-1, -300, 4000)));
        assertEquals(scoreDefinitionHSS.createScoreUninitialized(-77, 19, -320, 0),
                scoreDefinitionHSS.createScoreUninitialized(-70, 20, -20, -4000).add(
                        scoreDefinitionHSS.createScoreUninitialized(-7, -1, -300, 4000)));
    }

    @Test
    public void subtractHSS() {
        assertEquals(scoreDefinitionHSS.createScore(21, 280, -8000),
                scoreDefinitionHSS.createScore(20, -20, -4000).subtract(
                        scoreDefinitionHSS.createScore(-1, -300, 4000)));
        assertEquals(scoreDefinitionHSS.createScoreUninitialized(-63, 21, 280, -8000),
                scoreDefinitionHSS.createScoreUninitialized(-70, 20, -20, -4000).subtract(
                        scoreDefinitionHSS.createScoreUninitialized(-7, -1, -300, 4000)));
    }

    @Test
    public void multiplyHSS() {
        assertEquals(scoreDefinitionHSS.createScore(6, -6, 6),
                scoreDefinitionHSS.createScore(5, -5, 5).multiply(1.2));
        assertEquals(scoreDefinitionHSS.createScore(1, -2, 1),
                scoreDefinitionHSS.createScore(1, -1, 1).multiply(1.2));
        assertEquals(scoreDefinitionHSS.createScore(4, -5, 4),
                scoreDefinitionHSS.createScore(4, -4, 4).multiply(1.2));
        assertEquals(scoreDefinitionHSS.createScoreUninitialized(-14, 8, -10, 12),
                scoreDefinitionHSS.createScoreUninitialized(-7, 4, -5, 6).multiply(2.0));
    }

    @Test
    public void divideHSS() {
        assertEquals(scoreDefinitionHSS.createScore(5, -5, 5),
                scoreDefinitionHSS.createScore(25, -25, 25).divide(5.0));
        assertEquals(scoreDefinitionHSS.createScore(4, -5, 4),
                scoreDefinitionHSS.createScore(21, -21, 21).divide(5.0));
        assertEquals(scoreDefinitionHSS.createScore(4, -5, 4),
                scoreDefinitionHSS.createScore(24, -24, 24).divide(5.0));
        assertEquals(scoreDefinitionHSS.createScoreUninitialized(-7, 4, -5, 6),
                scoreDefinitionHSS.createScoreUninitialized(-14, 8, -10, 12).divide(2.0));
    }

    @Test
    public void powerHSS() {
        assertEquals(scoreDefinitionHSS.createScore(9, 16, 25),
                scoreDefinitionHSS.createScore(3, -4, 5).power(2.0));
        assertEquals(scoreDefinitionHSS.createScore(3, 4, 5),
                scoreDefinitionHSS.createScore(9, 16, 25).power(0.5));
        assertEquals(scoreDefinitionHSS.createScoreUninitialized(-343, 27, -64, 125),
                scoreDefinitionHSS.createScoreUninitialized(-7, 3, -4, 5).power(3.0));
    }

    @Test
    public void negateHSS() {
        assertEquals(scoreDefinitionHSS.createScore(-3, 4, -5),
                scoreDefinitionHSS.createScore(3, -4, 5).negate());
        assertEquals(scoreDefinitionHSS.createScore(3, -4, 5),
                scoreDefinitionHSS.createScore(-3, 4, -5).negate());
    }

    @Test
    public void equalsAndHashCodeHSS() {
        assertScoresEqualsAndHashCode(
                scoreDefinitionHSS.createScore(-10, -200, -3000),
                scoreDefinitionHSS.createScore(-10, -200, -3000),
                scoreDefinitionHSS.createScoreUninitialized(0, -10, -200, -3000)
        );
        assertScoresEqualsAndHashCode(
                scoreDefinitionHSS.createScoreUninitialized(-7, -10, -200, -3000),
                scoreDefinitionHSS.createScoreUninitialized(-7, -10, -200, -3000)
        );
        assertScoresNotEquals(
                scoreDefinitionHSS.createScore(-10, -200, -3000),
                scoreDefinitionHSS.createScore(-30, -200, -3000),
                scoreDefinitionHSS.createScore(-10, -400, -3000),
                scoreDefinitionHSS.createScore(-10, -400, -5000),
                scoreDefinitionHSS.createScoreUninitialized(-7, -10, -200, -3000)
        );
    }

    @Test
    public void compareToHSS() {
        PlannerAssert.assertCompareToOrder(
                scoreDefinitionHSS.createScoreUninitialized(-8, 0, 0, 0),
                scoreDefinitionHSS.createScoreUninitialized(-7, -20, -20, -20),
                scoreDefinitionHSS.createScoreUninitialized(-7, -1, -300, -4000),
                scoreDefinitionHSS.createScoreUninitialized(-7, 0, 0, 0),
                scoreDefinitionHSS.createScoreUninitialized(-7, 0, 0, 1),
                scoreDefinitionHSS.createScoreUninitialized(-7, 0, 1, 0),
                scoreDefinitionHSS.createScore(-20, Integer.MIN_VALUE, Integer.MIN_VALUE),
                scoreDefinitionHSS.createScore(-20, Integer.MIN_VALUE, -20),
                scoreDefinitionHSS.createScore(-20, Integer.MIN_VALUE, 1),
                scoreDefinitionHSS.createScore(-20, -300, -4000),
                scoreDefinitionHSS.createScore(-20, -300, -300),
                scoreDefinitionHSS.createScore(-20, -300, -20),
                scoreDefinitionHSS.createScore(-20, -300, 300),
                scoreDefinitionHSS.createScore(-20, -20, -300),
                scoreDefinitionHSS.createScore(-20, -20, 0),
                scoreDefinitionHSS.createScore(-20, -20, 1),
                scoreDefinitionHSS.createScore(-1, -300, -4000),
                scoreDefinitionHSS.createScore(-1, -300, -20),
                scoreDefinitionHSS.createScore(-1, -20, -300),
                scoreDefinitionHSS.createScore(1, Integer.MIN_VALUE, -20),
                scoreDefinitionHSS.createScore(1, -20, Integer.MIN_VALUE)
        );
    }

    private BendableScoreDefinition scoreDefinitionHHSSS = new BendableScoreDefinition(2, 3);

    @Test
    public void feasibleHHSSS() {
        assertScoreNotFeasible(
                scoreDefinitionHHSSS.createScore(-1, -20, -300, -4000, -5000),
                scoreDefinitionHHSSS.createScore(-1, 0, -300, -4000, -5000),
                scoreDefinitionHHSSS.createScore(-1, 20, -300, -4000, -5000),
                scoreDefinitionHHSSS.createScore(0, -20, -300, -4000, -5000),
                scoreDefinitionHHSSS.createScore(1, -20, -300, -4000, -5000)
        );
        assertScoreFeasible(
                scoreDefinitionHHSSS.createScore(0, 0, -300, -4000, -5000),
                scoreDefinitionHHSSS.createScore(0, 20, -300, -4000, -5000),
                scoreDefinitionHHSSS.createScore(1, 0, -300, -4000, -5000),
                scoreDefinitionHHSSS.createScore(1, 20, -300, -4000, -5000)
        );
    }

    @Test
    public void addHHSSS() {
        assertEquals(scoreDefinitionHHSSS.createScore(19, -320, 0, 0, 0),
                scoreDefinitionHHSSS.createScore(20, -20, -4000, 0, 0).add(
                        scoreDefinitionHHSSS.createScore(-1, -300, 4000, 0, 0)));
    }

    @Test
    public void subtractHHSSS() {
        assertEquals(scoreDefinitionHHSSS.createScore(21, 280, -8000, 0, 0),
                scoreDefinitionHHSSS.createScore(20, -20, -4000, 0, 0).subtract(
                        scoreDefinitionHHSSS.createScore(-1, -300, 4000, 0, 0)));
    }

    @Test
    public void multiplyHHSSS() {
        assertEquals(scoreDefinitionHHSSS.createScore(6, -6, 6, 0, 0),
                scoreDefinitionHHSSS.createScore(5, -5, 5, 0, 0).multiply(1.2));
        assertEquals(scoreDefinitionHHSSS.createScore(1, -2, 1, 0, 0),
                scoreDefinitionHHSSS.createScore(1, -1, 1, 0, 0).multiply(1.2));
        assertEquals(scoreDefinitionHHSSS.createScore(4, -5, 4, 0, 0),
                scoreDefinitionHHSSS.createScore(4, -4, 4, 0, 0).multiply(1.2));
    }

    @Test
    public void divideHHSSS() {
        assertEquals(scoreDefinitionHHSSS.createScore(5, -5, 5, 0, 0),
                scoreDefinitionHHSSS.createScore(25, -25, 25, 0, 0).divide(5.0));
        assertEquals(scoreDefinitionHHSSS.createScore(4, -5, 4, 0, 0),
                scoreDefinitionHHSSS.createScore(21, -21, 21, 0, 0).divide(5.0));
        assertEquals(scoreDefinitionHHSSS.createScore(4, -5, 4, 0, 0),
                scoreDefinitionHHSSS.createScore(24, -24, 24, 0, 0).divide(5.0));
    }

    @Test
    public void powerHHSSS() {
        assertEquals(scoreDefinitionHHSSS.createScore(9, 16, 25, 0, 0),
                scoreDefinitionHHSSS.createScore(3, -4, 5, 0, 0).power(2.0));
        assertEquals(scoreDefinitionHHSSS.createScore(3, 4, 5, 0, 0),
                scoreDefinitionHHSSS.createScore(9, 16, 25, 0, 0).power(0.5));
    }

    @Test
    public void negateHHSSS() {
        assertEquals(scoreDefinitionHHSSS.createScore(-3, 4, -5, 0, 0),
                scoreDefinitionHHSSS.createScore(3, -4, 5, 0, 0).negate());
        assertEquals(scoreDefinitionHHSSS.createScore(3, -4, 5, 0, 0),
                scoreDefinitionHHSSS.createScore(-3, 4, -5, 0, 0).negate());
    }

    @Test
    public void equalsAndHashCodeHHSSS() {
        assertScoresEqualsAndHashCode(
                scoreDefinitionHHSSS.createScore(-10, -20, -30, 0, 0),
                scoreDefinitionHHSSS.createScore(-10, -20, -30, 0, 0)
        );
    }

    @Test
    public void compareToHHSSS() {
        PlannerAssert.assertCompareToOrder(
                scoreDefinitionHHSSS.createScore(-20, Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0),
                scoreDefinitionHHSSS.createScore(-20, Integer.MIN_VALUE, -20, 0, 0),
                scoreDefinitionHHSSS.createScore(-20, Integer.MIN_VALUE, 1, 0, 0),
                scoreDefinitionHHSSS.createScore(-20, -300, -4000, 0, 0),
                scoreDefinitionHHSSS.createScore(-20, -300, -300, 0, 0),
                scoreDefinitionHHSSS.createScore(-20, -300, -20, 0, 0),
                scoreDefinitionHHSSS.createScore(-20, -300, 300, 0, 0),
                scoreDefinitionHHSSS.createScore(-20, -20, -300, 0, 0),
                scoreDefinitionHHSSS.createScore(-20, -20, 0, 0, 0),
                scoreDefinitionHHSSS.createScore(-20, -20, 1, 0, 0),
                scoreDefinitionHHSSS.createScore(-1, -300, -4000, 0, 0),
                scoreDefinitionHHSSS.createScore(-1, -300, -20, 0, 0),
                scoreDefinitionHHSSS.createScore(-1, -20, -300, 0, 0),
                scoreDefinitionHHSSS.createScore(1, Integer.MIN_VALUE, -20, 0, 0),
                scoreDefinitionHHSSS.createScore(1, -20, Integer.MIN_VALUE, 0, 0)
        );
    }

    @Test
    public void serializeAndDeserialize() {
        PlannerTestUtils.serializeAndDeserializeWithAll(
                scoreDefinitionHSS.createScore(-12, 3400, -56),
                output -> {
                    assertEquals(0, output.getInitScore());
                    assertEquals(-12, output.getHardScore(0));
                    assertEquals(3400, output.getSoftScore(0));
                    assertEquals(-56, output.getSoftScore(1));
                }
        );
        PlannerTestUtils.serializeAndDeserializeWithAll(
                scoreDefinitionHSS.createScoreUninitialized(-7, -12, 3400, -56),
                output -> {
                    assertEquals(-7, output.getInitScore());
                    assertEquals(-12, output.getHardScore(0));
                    assertEquals(3400, output.getSoftScore(0));
                    assertEquals(-56, output.getSoftScore(1));
                }
        );
    }

}
