// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.pgbulkinsert.test.pgsql.handlers;

import de.bytefish.pgbulkinsert.PgBulkInsert;
import de.bytefish.pgbulkinsert.mapping.AbstractMapping;
import de.bytefish.pgbulkinsert.pgsql.model.interval.Interval;
import de.bytefish.pgbulkinsert.test.utils.TransactionalTestBase;
import de.bytefish.pgbulkinsert.util.PostgreSqlUtils;
import org.junit.Assert;
import org.junit.Test;
import org.postgresql.util.PGInterval;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PgBulkInsertTest extends TransactionalTestBase {

    private static class SampleEntity {

        public Integer col_integer;
        public LocalDate col_date;
        public LocalTime col_time;
        public LocalDateTime col_datetime;
        public Float col_float;
        public Double col_double;
        public String col_text;
        public Long col_long;
        public Short col_short;
        public UUID col_uuid;
        public Inet4Address col_inet4Address;
        public Inet6Address col_inet6Address;
        public byte[] col_bytearray;
        public Boolean col_boolean;
        public List<Integer> col_int_array;
        public List<Double> col_double_array;
        public String col_jsonb;
        public BigDecimal col_numeric;
        public BigInteger col_bignumeric;
        public Interval col_interval;

        public Interval get_col_interval() {
            return col_interval;
        }

        public Integer get_col_integer() {
            return col_integer;
        }

        public LocalDateTime get_col_datetime() {
            return col_datetime;
        }

        public Float get_col_float() {
            return col_float;
        }

        public Double get_col_double() {
            return col_double;
        }

        public String get_col_text() {
            return col_text;
        }

        public Long get_col_long() {
            return col_long;
        }

        public Short get_col_short() {
            return col_short;
        }

        public UUID get_col_uuid() {
            return col_uuid;
        }

        public LocalDate getCol_date() {
            return col_date;
        }

        public LocalTime getCol_time() {
            return col_time;
        }

        public Inet4Address getCol_inet4Address() {
            return col_inet4Address;
        }

        public Inet6Address getCol_inet6Address() {
            return col_inet6Address;
        }

        public byte[] getCol_bytearray() {
            return col_bytearray;
        }

        public Boolean getCol_boolean() {
            return col_boolean;
        }

        public List<Integer> getCol_int_array() {
            return col_int_array;
        }

        public List<Double> getCol_double_array() {
            return col_double_array;
        }

        public String getCol_jsonb() {
            return col_jsonb;
        }

        public BigDecimal getCol_numeric() {
            return col_numeric;
        }

        public BigInteger getCol_bignumeric() {
            return col_bignumeric;
        }

    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        createTable();
    }

    @Override
    protected void onSetUpBeforeTransaction() throws Exception {

    }

    private class SampleEntityMapping extends AbstractMapping<SampleEntity> {

        public SampleEntityMapping() {
            super(schema, "unit_test");

            mapText("col_text", SampleEntity::get_col_text);
            mapInteger("col_integer", SampleEntity::get_col_integer);
            mapShort("col_smallint", SampleEntity::get_col_short);
            mapTimeStamp("col_timestamp", SampleEntity::get_col_datetime);
            mapLong("col_bigint", SampleEntity::get_col_long);
            mapDate("col_date", SampleEntity::getCol_date);
            mapTime("col_time", SampleEntity::getCol_time);
            mapInet4Addr("col_inet4", SampleEntity::getCol_inet4Address);
            mapInet6Addr("col_inet6", SampleEntity::getCol_inet6Address);
            mapUUID("col_uuid", SampleEntity::get_col_uuid);
            mapByteArray("col_bytea", SampleEntity::getCol_bytearray);
            mapDouble("col_double", SampleEntity::get_col_double);
            mapFloat("col_real", SampleEntity::get_col_float);
            mapBoolean("col_boolean", SampleEntity::getCol_boolean);
            mapIntegerArray("col_int_array", SampleEntity::getCol_int_array);
            mapDoubleArray("col_double_array", SampleEntity::getCol_double_array);
            mapJsonb("col_jsonb", SampleEntity::getCol_jsonb);
            mapNumeric("col_numeric", SampleEntity::getCol_numeric);
            mapNumeric("col_bignumeric", SampleEntity::getCol_bignumeric);
            mapInterval("col_interval", SampleEntity::get_col_interval);
        }
    }

    @Test
    public void saveAll_interval_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();

        // 2 mons 15 days 02:03:04.005
        entity.col_interval = new Interval(2, 15, 7384005000L);

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            PGInterval v = (PGInterval) rs.getObject("col_interval");

            Assert.assertEquals(entity.col_interval.getDays(), v.getDays());
            Assert.assertEquals(entity.col_interval.getMonths(), v.getMonths());
            Assert.assertEquals(5000, v.getMicroSeconds());
        }
    }

    @Test
    public void saveAll_interval_AlternateConstructor_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();

        // 2 mons 15 days 02:03:04.005
        entity.col_interval = new Interval(2, 15, 2, 3, 4, 5000);

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            PGInterval v = (PGInterval) rs.getObject("col_interval");

            Assert.assertEquals(entity.col_interval.getDays(), v.getDays());
            Assert.assertEquals(entity.col_interval.getMonths(), v.getMonths());
            Assert.assertEquals(2, v.getHours());
            Assert.assertEquals(3, v.getMinutes());
            Assert.assertEquals(4, v.getWholeSeconds());
            Assert.assertEquals(5000, v.getMicroSeconds());
        }
    }


    @Test
    public void saveAll_numeric_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();
        entity.col_numeric = new BigDecimal("210000.00011234567");

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            BigDecimal v = rs.getBigDecimal("col_numeric");


            Assert.assertEquals(new BigDecimal("210000.00011234567"), v.stripTrailingZeros());
        }
    }


    @Test
    public void saveAll_BigNumeric_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();
        entity.col_bignumeric = new BigInteger("999999999999999999999999999999999999");

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            BigDecimal v = rs.getBigDecimal("col_bignumeric");

            Assert.assertEquals(new BigInteger("999999999999999999999999999999999999"), v.toBigInteger());
        }
    }

    @Test
    public void saveAll_boolean_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();
        entity.col_boolean = true;

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            boolean v = rs.getBoolean("col_boolean");

            Assert.assertEquals(true, v);
        }
    }

    @Test
    public void saveAll_Short_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();
        entity.col_short = 1;

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            short v = rs.getShort("col_smallint");

            Assert.assertEquals(1, v);
        }
    }

    @Test
    public void saveAll_Integer_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();
        entity.col_integer = 1;

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            int v = rs.getInt("col_integer");

            Assert.assertEquals(1, v);
        }
    }

    @Test
    public void saveAll_Double_Precision_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();
        entity.col_double = 2.0001;

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            double v = rs.getDouble("col_double");

            Assert.assertEquals(2.0001, v, 1e-10);
        }
    }

    @Test
    public void saveAll_Single_Precision_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();
        entity.col_float = 2.0001f;

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            float v = rs.getFloat("col_real");

            Assert.assertEquals(2.0001, v, 1e-6);
        }
    }

    @Test
    public void saveAll_LocalDateTime_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();
        entity.col_datetime = LocalDateTime.of(2010, 1, 1, 0, 0, 0, 10000);

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            Timestamp v = rs.getTimestamp("col_timestamp");

            Assert.assertEquals(LocalDateTime.of(2010, 1, 1, 0, 0, 0, 10000), v.toLocalDateTime());
        }
    }

    @Test
    public void saveAll_LocalDateTime_Before_Postgres_Epoch_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();
        entity.col_datetime = LocalDateTime.of(1712, 1, 3, 0, 0, 0, 10000);

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            Timestamp v = rs.getTimestamp("col_timestamp");

            Assert.assertEquals(LocalDateTime.of(1712, 1, 3, 0, 0, 0, 10000), v.toLocalDateTime());
        }
    }

    @Test
    public void saveAll_LocalDate_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();
        entity.col_date = LocalDate.of(2010, 1, 1);

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            Timestamp v = rs.getTimestamp("col_date");

            Assert.assertEquals(LocalDateTime.of(2010, 1, 1, 0, 0, 0), v.toLocalDateTime());
        }
    }

    @Test
    public void saveAll_LocalTime_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();
        entity.col_time = LocalTime.of(10, 10);

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            Time v = rs.getTime("col_time");

            Assert.assertEquals(LocalTime.of(10, 10), v.toLocalTime());
        }
    }

    @Test
    public void saveAll_String_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();
        entity.col_text = "ABC";

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            String v = rs.getString("col_text");

            Assert.assertEquals("ABC", v);
        }
    }


    @Test
    public void saveAll_Long_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();
        entity.col_long = 1L;

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            long v = rs.getLong("col_bigint");
            Assert.assertEquals(1, v);
        }
    }


    @Test
    public void saveAll_Jsonb_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Json To Store:
        String jsonText = "{\"bar\": \"baz\", \"balance\": 7.77}";

        String expected_1 = "{\"bar\": \"baz\", \"balance\": 7.77}";
        String expected_2 = "{\"balance\": 7.77, \"bar\": \"baz\"}";

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();
        entity.col_jsonb = jsonText;

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            String v = rs.getString("col_jsonb");


            Assert.assertTrue(expected_1.equals(v) || expected_2.equals(v));
        }
    }

    @Test
    public void saveAll_Inet4_Test() throws SQLException, UnknownHostException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();
        entity.col_inet4Address = (Inet4Address) Inet4Address.getByName("127.0.0.1");

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            String v = rs.getString("col_inet4");
            Assert.assertEquals("127.0.0.1", v);
        }
    }

    @Test
    public void saveAll_UUID_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        UUID uuid = UUID.randomUUID();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();
        entity.col_uuid = uuid;

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            String v = rs.getString("col_uuid");
            Assert.assertEquals(uuid.toString(), v);
        }
    }

    @Test
    public void saveAll_Inet6_Test() throws SQLException, UnknownHostException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();
        entity.col_inet6Address = (Inet6Address) Inet6Address.getByName("1080::8:800:200c:417a");

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            String v = rs.getString("col_inet6");
            Assert.assertEquals("1080::8:800:200c:417a", v);
        }
    }

    @Test
    public void saveAll_ByteArray_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();

        byte byte1 = 1;
        byte byte2 = 2;

        entity.col_bytearray = new byte[]{byte1, byte2};

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            byte[] v = rs.getBytes("col_bytea");

            Assert.assertEquals((byte) 1, v[0]);
            Assert.assertEquals((byte) 2, v[1]);
        }
    }

    @Test
    public void saveAll_CustomIntegerArray_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();
        entity.col_int_array = Arrays.asList(1, 2);

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            Array z = rs.getArray("col_int_array");

            Integer[] v = (Integer[]) z.getArray();

            Assert.assertEquals((Integer) 1, v[0]);
            Assert.assertEquals((Integer) 2, v[1]);
        }
    }

    @Test
    public void saveAll_CustomDoubleArray_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entity to insert:
        SampleEntity entity = new SampleEntity();
        entity.col_double_array = Arrays.asList(1.131, 2.412);

        entities.add(entity);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        ResultSet rs = getAll();

        while (rs.next()) {
            Array z = rs.getArray("col_double_array");

            Double[] v = (Double[]) z.getArray();

            Assert.assertEquals(1.131, v[0], 1e-12);
            Assert.assertEquals(2.412, v[1], 1e-12);
        }
    }

    @Test
    public void saveAll_Multiple_Entities_Test() throws SQLException {

        // This list will be inserted.
        List<SampleEntity> entities = new ArrayList<>();

        // Create the Entities to insert:
        SampleEntity entity0 = new SampleEntity();
        entity0.col_long = 1L;

        SampleEntity entity1 = new SampleEntity();
        entity1.col_long = 2L;

        entities.add(entity0);
        entities.add(entity1);

        PgBulkInsert<SampleEntity> pgBulkInsert = new PgBulkInsert<>(new SampleEntityMapping());

        pgBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities.stream());

        Assert.assertEquals(2, getRowCount());

        ResultSet rs = getAll();

        // Turn it into a List:
        ArrayList<Long> values = new ArrayList<>();

        while (rs.next()) {
            values.add(rs.getLong("col_bigint"));
        }

        Assert.assertTrue(values.stream().anyMatch(x -> x == 1));
        Assert.assertTrue(values.stream().anyMatch(x -> x == 2));
    }

    private ResultSet getAll() throws SQLException {
        String sqlStatement = String.format("SELECT * FROM %s.unit_test", schema);

        Statement statement = connection.createStatement();

        return statement.executeQuery(sqlStatement);
    }

    private boolean createTable() throws SQLException {
        String sqlStatement = String.format("CREATE TABLE %s.unit_test\n", schema) +
                "            (\n" +
                "                col_smallint smallint,\n" +
                "                col_integer integer,\n" +
                "                col_money money,\n" +
                "                col_bigint bigint,\n" +
                "                col_long bigint,\n" +
                "                col_timestamp timestamp,\n" +
                "                col_real real,\n" +
                "                col_double double precision,\n" +
                "                col_bytea bytea,\n" +
                "                col_uuid uuid,\n" +
                "                col_inet4 inet,\n" +
                "                col_inet6 inet,\n" +
                "                col_macaddr macaddr,\n" +
                "                col_date date,\n" +
                "                col_time time,\n" +
                "                col_interval interval,\n" +
                "                col_boolean boolean,\n" +
                "                col_text text,\n" +
                "                col_int_array integer[], \n" +
                "                col_double_array double precision[], \n" +
                "                col_jsonb jsonb, \n" +
                "                col_numeric numeric(50, 20), \n" +
                "                col_bignumeric numeric(38) \n" +
                "            );";

        Statement statement = connection.createStatement();

        return statement.execute(sqlStatement);
    }

    private int getRowCount() throws SQLException {

        Statement s = connection.createStatement();

        ResultSet r = s.executeQuery(String.format("SELECT COUNT(*) AS rowcount FROM %s.unit_test", schema));
        r.next();
        int count = r.getInt("rowcount");
        r.close();

        return count;
    }

}