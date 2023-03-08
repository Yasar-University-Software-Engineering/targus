/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.targus.algorithm.aco.problem.tsp.tsplib.stateparser;

public class KeywordAndValue {

    private final Keyword keyword;
    private final String value;

    public KeywordAndValue(Keyword keyword, String value) {
        this.keyword = keyword;
        this.value = value;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public String getValue() {
        return value;
    }
}