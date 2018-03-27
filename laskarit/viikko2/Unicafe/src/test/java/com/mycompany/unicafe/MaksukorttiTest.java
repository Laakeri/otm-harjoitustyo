package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void saldoOikein() {
        assertEquals(10, kortti.saldo());
    }
    
    @Test
    public void latausOikein() {
        kortti.lataaRahaa(5);
        assertEquals(15, kortti.saldo());
    }
    
    @Test
    public void saldoVahenee() {
        kortti.otaRahaa(5);
        assertEquals(5, kortti.saldo());
    }
    
    @Test
    public void saldoEiMuutu() {
        kortti.otaRahaa(11);
        assertEquals(10, kortti.saldo());
    }
    
    @Test
    public void vahennysTrue() {
        assertEquals(true, kortti.otaRahaa(9));
    }
    
    @Test
    public void vahennysFalse() {
        assertEquals(false, kortti.otaRahaa(11));
    }
    
    @Test
    public void toStringToimii() {
        assertEquals("saldo: 0.10", kortti.toString());
    }
    
    @Test
    public void toStringToimiiOikeasti() {
        kortti.otaRahaa(1);
        assertEquals("saldo: 0.09", kortti.toString());
    }
    
    @Test
    public void toStringToimiiNolla() {
        kortti.otaRahaa(10);
        assertEquals("saldo: 0", kortti.toString());
    }
}
