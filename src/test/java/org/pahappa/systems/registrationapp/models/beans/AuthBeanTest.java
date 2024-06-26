package org.pahappa.systems.registrationapp.models.beans;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit test for the Auth bean.
 */
public class AuthBeanTest
{
//    AuthBean bean = new AuthBean();
    /**
     * Rigorous Test :-)
     */
    @Test
    public void hashAlwaysWorks()
    {
        assertEquals(AuthBean.hexHashString("pass"),"5b722b307fce6c944905d132691d5e4a2214b7fe92b738920eb3fce3a90420a19511c3010a0e7712b054daef5b57bad59ecbd93b3280f210578f547f4aed4d25");
    }
}
