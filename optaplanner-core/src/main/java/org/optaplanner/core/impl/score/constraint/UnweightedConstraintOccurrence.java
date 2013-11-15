/*
 * Copyright 2010 JBoss Inc
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

package org.optaplanner.core.impl.score.constraint;

import org.optaplanner.core.api.score.constraint.primint.IntConstraintMatch;

/**
 * Will be removed in version 6.1.0.Beta1.
 * @Deprecated in favor of {@link IntConstraintMatch}
 */
@Deprecated
public class UnweightedConstraintOccurrence extends ConstraintOccurrence {

    public UnweightedConstraintOccurrence(String ruleId, Object... causes) {
        this(ruleId, ConstraintType.HARD, causes);
    }

    public UnweightedConstraintOccurrence(String ruleId, ConstraintType constraintType, Object... causes) {
        super(ruleId, constraintType, causes);
    }

}