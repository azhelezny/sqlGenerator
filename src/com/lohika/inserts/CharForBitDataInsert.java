package com.lohika.inserts;

/**
 * @author Andrey Zhelezny
 *         Date: 11/24/15
 */
public class CharForBitDataInsert extends AbstractInsert {
    public CharForBitDataInsert(String tableName) {
        super(tableName);
        fill(1);
    }

    public CharForBitDataInsert(String tableName, int size) {
        super(tableName);
        fill(size);
    }

    private void fill(int size) {
        values.clear();
        if (size == 1 || size < 40) {
            add("X'F5'");
            add("X'E8'");
            add("X'70'");
            add("X'47'");
            add("X'65'");
            add("X'84'");
            add("X'2D'");
            add("X'3F'");
            add("X'82'");
            add("X'0F'");
            add("X'CD'");
            add("X'C3'");
            add("X'A5'");
            add("X'E1'");
            add("X'0B'");
            add("X'F6'");
            add("X'22'");
            add("X'7D'");
            add("X'20'");
            add("X'F9'");
            add("X'29'");
            add("X'22'");
        }
        if (size < 100 && size >= 40) {
            add("X'1F50DC078F'");
            add("X'A45CAA4FA9'");
            add("X'B33A819EA3'");
            add("X'BDC2112249'");
            add("X'D3E28C28FD'");
            add("X'53547F480E'");
            add("X'EA9422347B'");
            add("X'4F65C4FE61'");
            add("X'BF9228FD8A'");
            add("X'B3E8FEAB68'");
            add("X'00B5C43995'");
            add("X'4857A6F82F'");
            add("X'21B25AFFE8'");
            add("X'2BD2522EDC'");
            add("X'626ACD5CD1'");
            add("X'F493DAA174'");
            add("X'1A8E1817DC'");
            add("X'149A09C526'");
            add("X'3CC7437845'");
            add("X'1A76DF48DA'");
            add("X'8B92ED7E24'");
            add("X'731D3EF3BE'");
            add("X'A7B7FA58E7'");
        }
        if (size >= 100) {
            add("X'B7A6A94096FD2B23'");
            add("X'016946E410EC24C9'");
            add("X'3F317CD25AEEB758'");
            add("X'04C3D6538F9EC03E'");
            add("X'E6E871A65BAE79FE'");
            add("X'5A55497EBF82C6E1'");
            add("X'B51049C74A2FD7DA'");
            add("X'B3D27487596E6820'");
            add("X'A8656317323ACC57'");
            add("X'CCF8C7C7CCB59FDE'");
            add("X'66D64C9667829EBA'");
            add("X'BC10AF4F5801EC00'");
            add("X'563E48C09CFC31FC'");
            add("X'A0132B3A657473A6'");
            add("X'2BDDD6E32A33E465'");
            add("X'E66DBA89199ACFEC'");
            add("X'2501A9F2FA66326F'");
            add("X'7D24E545B9C983A9'");
            add("X'7517AF46B4420F67'");
            add("X'55DA7A7F5AA22888'");
            add("X'47001BB36CA61F29'");
            add("X'3E785D4998791E00'");
            add("X'5C64C6C8101324C7'");
            add("X'4092246D0AEBFDA2'");
            add("X'A6F36C216795D95F'");
            add("X'ED08CB7EC4AB7732'");
        }
        add("NULL");
    }
}

