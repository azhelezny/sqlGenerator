package com.lohika;

import com.lohika.inserts.BigintInsert;
import com.lohika.tests.LengthWidthTest;
import com.lohika.tests.MaxWidthTest;
import com.lohika.tests.constraints.check.ConstraintsUniqueTest;
import com.lohika.tests.constraints.check.PrimaryKeysMultColumsConstraints;
import com.lohika.tests.constraints.check.ConstraintsSingleColumnTest;

public class Main {

    public static void main(String[] args) {
        // write your code here
        //String pattern = TestPattern.LENGTH_WIDTH_TEST_PATTERN;
        //LengthWidthTest.createTests();
        //BigintInsert ins = new BigintInsert("pishj");
        //MaxWidthTest.createTests();
        ConstraintsSingleColumnTest.createTests();

        PrimaryKeysMultColumsConstraints.createTests();
    }
}
