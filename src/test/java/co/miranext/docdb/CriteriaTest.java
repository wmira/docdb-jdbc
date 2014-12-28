package co.miranext.docdb;

import co.miranext.docdb.postgresql.PsqlJsonFieldCriterion;
import co.miranext.docdb.sql.SQLBuilder;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 */
public class CriteriaTest {

    @Test
    public void testCriteriaSQLStrings() {

        ColumnCriterion cr = new ColumnCriterion("column","test");

        assertEquals("column",cr.getColumn());
        assertEquals("test",cr.getValue());
        assertEquals("column=?",cr.toSQLString());

        FieldCriterion fieldCriterion = new PsqlJsonFieldCriterion("column","id","abc");

        assertEquals("id",fieldCriterion.getField());
        assertEquals("abc",fieldCriterion.getValue());
        assertEquals("column->>'id'=?",fieldCriterion.toSQLString());


        Criteria criteria = new Criteria();
        criteria.add(cr);
        criteria.add(fieldCriterion);

        assertEquals("column=?" + Criteria.SQL_STMT_DELIM + "column->>'id'=?",criteria.toSQLString());

        ColumnExtra extra = new ColumnExtra("col");
        ColumnExtra extraAuto = new ColumnExtra("auto:record_id");

        assertEquals("col",extra.getColumn());
        assertFalse(extra.isAuto());


        assertEquals("record_id", extraAuto.getColumn());
        assertTrue(extraAuto.isAuto());
    }

    @Test
    public void testSqlBuilder() {


        Criteria criteria = new Criteria();
        criteria.add(new PsqlJsonFieldCriterion("column","id","abc"));

        DocumentMeta meta = new DocumentMeta("tbl","data","id",new String[]{"auto:record","account_id"});

        String builderSelectRes = "SELECT data  , record , account_id FROM tbl  WHERE column->>'id'=?";

        assertEquals(builderSelectRes,SQLBuilder.createSqlSelect(meta,criteria));

    }
}