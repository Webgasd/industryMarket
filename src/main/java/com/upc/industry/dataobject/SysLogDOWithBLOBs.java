package com.upc.industry.dataobject;

public class SysLogDOWithBLOBs extends SysLogDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_log.old_value
     *
     * @mbg.generated Tue Jul 21 10:06:03 CST 2020
     */
    private String oldValue;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_log.new_value
     *
     * @mbg.generated Tue Jul 21 10:06:03 CST 2020
     */
    private String newValue;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_log.old_value
     *
     * @return the value of sys_log.old_value
     *
     * @mbg.generated Tue Jul 21 10:06:03 CST 2020
     */
    public String getOldValue() {
        return oldValue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_log.old_value
     *
     * @param oldValue the value for sys_log.old_value
     *
     * @mbg.generated Tue Jul 21 10:06:03 CST 2020
     */
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue == null ? null : oldValue.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_log.new_value
     *
     * @return the value of sys_log.new_value
     *
     * @mbg.generated Tue Jul 21 10:06:03 CST 2020
     */
    public String getNewValue() {
        return newValue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_log.new_value
     *
     * @param newValue the value for sys_log.new_value
     *
     * @mbg.generated Tue Jul 21 10:06:03 CST 2020
     */
    public void setNewValue(String newValue) {
        this.newValue = newValue == null ? null : newValue.trim();
    }
}