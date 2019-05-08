package com.lcg.base.surpport;

import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

/**
 * Created by johnny on 2019/5/8.
 * 配置
 *
 * @author johnny
 */
public class Config {
    private String UUID;
    private String IDENTITY;
    private boolean BEFORE;
    private String seqFormat;
    private String catalog;
    private String schema;
    private boolean checkExampleEntityClass;
    private boolean useSimpleType;
    private boolean enableMethodAnnotation;
    private boolean notEmpty = false;
    private Style style;

    public Config() {
    }

    public boolean isBEFORE() {
        return this.BEFORE;
    }

    public void setBEFORE(boolean BEFORE) {
        this.BEFORE = BEFORE;
    }

    public void setOrder(String order) {
        this.BEFORE = "BEFORE".equalsIgnoreCase(order);
    }

    public String getCatalog() {
        return this.catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getIDENTITY() {
        return StringUtils.isNotEmpty(this.IDENTITY) ? this.IDENTITY : IdentityDialect.MYSQL.getIdentityRetrievalStatement();
    }

    public void setIDENTITY(String IDENTITY) {
        IdentityDialect identityDialect = IdentityDialect.getDatabaseDialect(IDENTITY);
        if (identityDialect != null) {
            this.IDENTITY = identityDialect.getIdentityRetrievalStatement();
        } else {
            this.IDENTITY = IDENTITY;
        }

    }





    public String getSchema() {
        return this.schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSeqFormat() {
        return StringUtils.isNotEmpty(this.seqFormat) ? this.seqFormat : "{0}.nextval";
    }

    public void setSeqFormat(String seqFormat) {
        this.seqFormat = seqFormat;
    }

    public String getUUID() {
        return StringUtils.isNotEmpty(this.UUID) ? this.UUID : "@java.util.UUID@randomUUID().toString().replace(\"-\", \"\")";
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public boolean isNotEmpty() {
        return this.notEmpty;
    }

    public void setNotEmpty(boolean notEmpty) {
        this.notEmpty = notEmpty;
    }

    public Style getStyle() {
        return this.style == null ? Style.normal : this.style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public boolean isEnableMethodAnnotation() {
        return this.enableMethodAnnotation;
    }

    public void setEnableMethodAnnotation(boolean enableMethodAnnotation) {
        this.enableMethodAnnotation = enableMethodAnnotation;
    }

    public boolean isCheckExampleEntityClass() {
        return this.checkExampleEntityClass;
    }

    public void setCheckExampleEntityClass(boolean checkExampleEntityClass) {
        this.checkExampleEntityClass = checkExampleEntityClass;
    }

    public boolean isUseSimpleType() {
        return this.useSimpleType;
    }

    public void setUseSimpleType(boolean useSimpleType) {
        this.useSimpleType = useSimpleType;
    }

    public String getPrefix() {
        if (StringUtils.isNotEmpty(this.catalog)) {
            return this.catalog;
        } else {
            return StringUtils.isNotEmpty(this.schema) ? this.schema : "";
        }
    }

    public void setProperties(Properties properties) {
        if (properties == null) {
            this.style = Style.camelhump;
        } else {
            String UUID = properties.getProperty("UUID");
            if (StringUtils.isNotEmpty(UUID)) {
                this.setUUID(UUID);
            }

            String IDENTITY = properties.getProperty("IDENTITY");
            if (StringUtils.isNotEmpty(IDENTITY)) {
                this.setIDENTITY(IDENTITY);
            }

            String seqFormat = properties.getProperty("seqFormat");
            if (StringUtils.isNotEmpty(seqFormat)) {
                this.setSeqFormat(seqFormat);
            }

            String catalog = properties.getProperty("catalog");
            if (StringUtils.isNotEmpty(catalog)) {
                this.setCatalog(catalog);
            }

            String schema = properties.getProperty("schema");
            if (StringUtils.isNotEmpty(schema)) {
                this.setSchema(schema);
            }

            String ORDER = properties.getProperty("ORDER");
            if (StringUtils.isNotEmpty(ORDER)) {
                this.setOrder(ORDER);
            }

            String notEmpty = properties.getProperty("notEmpty");
            if (StringUtils.isNotEmpty(notEmpty)) {
                this.notEmpty = notEmpty.equalsIgnoreCase("TRUE");
            }

            String enableMethodAnnotation = properties.getProperty("enableMethodAnnotation");
            if (StringUtils.isNotEmpty(enableMethodAnnotation)) {
                this.enableMethodAnnotation = enableMethodAnnotation.equalsIgnoreCase("TRUE");
            }

            String checkExampleStr = properties.getProperty("checkExampleEntityClass");
            if (StringUtils.isNotEmpty(checkExampleStr)) {
                this.checkExampleEntityClass = checkExampleStr.equalsIgnoreCase("TRUE");
            }

            String useSimpleTypeStr = properties.getProperty("useSimpleType");
            if (StringUtils.isNotEmpty(useSimpleTypeStr)) {
                this.useSimpleType = useSimpleTypeStr.equalsIgnoreCase("TRUE");
            }

            String simpleTypes = properties.getProperty("simpleTypes");
            if (StringUtils.isNotEmpty(simpleTypes)) {
                SimpleTypeUtil.registerSimpleType(simpleTypes);
            }

            String styleStr = properties.getProperty("style");
            if (StringUtils.isNotEmpty(styleStr)) {
                try {
                    this.style = Style.valueOf(styleStr);
                } catch (IllegalArgumentException var15) {
                    throw new MapperException(styleStr + "不是合法的Style值!");
                }
            } else {
                this.style = Style.camelhump;
            }

        }
    }

}
