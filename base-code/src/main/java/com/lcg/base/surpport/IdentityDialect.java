package com.lcg.base.surpport;

/**
 * Created by johnny on 2019/5/8.
 * 获取?（插入后）?自增长主键的方言
 *
 * @author johnny
 */
public enum IdentityDialect {
    /**
     * DB2
     */
    DB2("VALUES IDENTITY_VAL_LOCAL()"),
    /**
     * MYSQL
     */
    MYSQL("SELECT LAST_INSERT_ID()"),
    /**
     * SQLSERVER
     */
    SQLSERVER("SELECT SCOPE_IDENTITY()"),
    /**
     * CLOUDSCAPE
     */
    CLOUDSCAPE("VALUES IDENTITY_VAL_LOCAL()"),
    /**
     * DERBY
     */
    DERBY("VALUES IDENTITY_VAL_LOCAL()"),
    /**
     * HSQLDB
     */
    HSQLDB("CALL IDENTITY()"),
    /**
     * SYBASE
     */
    SYBASE("SELECT @@IDENTITY"),
    /**
     * DB2_MF
     */
    DB2_MF("SELECT IDENTITY_VAL_LOCAL() FROM SYSIBM.SYSDUMMY1"),
    /**
     * INFORMIX
     */
    INFORMIX("select dbinfo('sqlca.sqlerrd1') from systables where tabid=1");


    /**
     * 身份检索声明
     */
    private String identityRetrievalStatement;

    private IdentityDialect(String identityRetrievalStatement) {
        this.identityRetrievalStatement = identityRetrievalStatement;
    }


    /**
     * 根据数据库获取对应方言
     *
     * @param database 数据库
     * @return
     */
    public static IdentityDialect getDatabaseDialect(String database) {
        IdentityDialect returnValue = null;
        if ("DB2".equalsIgnoreCase(database)) {
            returnValue = DB2;
        } else if ("MySQL".equalsIgnoreCase(database)) {
            returnValue = MYSQL;
        } else if ("SqlServer".equalsIgnoreCase(database)) {
            returnValue = SQLSERVER;
        } else if ("Cloudscape".equalsIgnoreCase(database)) {
            returnValue = CLOUDSCAPE;
        } else if ("Derby".equalsIgnoreCase(database)) {
            returnValue = DERBY;
        } else if ("HSQLDB".equalsIgnoreCase(database)) {
            returnValue = HSQLDB;
        } else if ("SYBASE".equalsIgnoreCase(database)) {
            returnValue = SYBASE;
        } else if ("DB2_MF".equalsIgnoreCase(database)) {
            returnValue = DB2_MF;
        } else if ("Informix".equalsIgnoreCase(database)) {
            returnValue = INFORMIX;
        }

        return returnValue;
    }

    public String getIdentityRetrievalStatement() {
        return this.identityRetrievalStatement;
    }
}
