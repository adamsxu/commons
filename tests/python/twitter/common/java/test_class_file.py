# ==================================================================================================
# Copyright 2011 Twitter, Inc.
# --------------------------------------------------------------------------------------------------
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this work except in compliance with the License.
# You may obtain a copy of the License in the LICENSE file, or at:
#
#  http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ==================================================================================================

import pkgutil
import pytest
from twitter.common.java.class_file import ClassFile

import unittest2 as unittest

# Known golden file, com.google.protobuf.ByteString => ByteString.class
# named example_class because of science .gitignore
#
# resources don't properly work in python_tests yet, so this test is marked as expected fail.
_EXAMPLE_RESOURCE = 'resources/example_class'

class ClassFileParserTest(unittest.TestCase):
  @classmethod
  def setup_class(cls):
    cls._class_data = pkgutil.get_data('twitter.common.java', _EXAMPLE_RESOURCE)
    assert cls._class_data is not None
    cls._class_file = ClassFile(cls._class_data)

  def test_parsed(self):
    assert self._class_file is not None

  def test_parsed_maj_min(self):
    maj, min = self._class_file.version()
    assert maj == 50
    assert min == 0

  def test_parsed_this_class(self):
    assert self._class_file.this_class() == 'com/google/protobuf/ByteString'

  def test_parsed_super_class(self):
    assert self._class_file.super_class() == 'java/lang/Object'

  def test_parsed_access(self):
    access_flags = self._class_file.access_flags()
    assert access_flags.public()
    assert access_flags.final()
    assert access_flags.super_()
    assert not access_flags.interface()
    assert not access_flags.abstract()

