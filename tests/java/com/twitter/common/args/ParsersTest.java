// =================================================================================================
// Copyright 2011 Twitter, Inc.
// -------------------------------------------------------------------------------------------------
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this work except in compliance with the License.
// You may obtain a copy of the License in the LICENSE file, or at:
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// =================================================================================================

package com.twitter.common.args;

import com.google.common.collect.ImmutableMap;

import org.junit.Before;
import org.junit.Test;

import com.twitter.common.args.parsers.PairParser;
import com.twitter.common.args.parsers.StringParser;
import com.twitter.common.collections.Pair;
import com.twitter.common.reflect.TypeToken;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * @author John Sirois
 */
public class ParsersTest {

  private Parsers defaultParsers;

  @Before
  public void setUp() {
    defaultParsers =
        new Parsers(ImmutableMap.<Class<?>, Parser<?>>of(
            String.class, new StringParser(),
            Pair.class, new PairParser()));
  }

  @Test
  public void testParseTypeFamily() {
    assertNotNull(defaultParsers.get(TypeToken.create(String.class)));

    class Credentials extends Pair<String, String> {
      public Credentials(String first, String second) {
        super(first, second);
      }
    }
    Parser parser = defaultParsers.get(TypeToken.create(Credentials.class));
    assertNotNull(parser);
    assertSame(parser, defaultParsers.get(TypeToken.create(Pair.class)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNoParser() {
    class NoParserForMe {}
    assertNull(defaultParsers.get(TypeToken.create(NoParserForMe.class)));
  }
}
