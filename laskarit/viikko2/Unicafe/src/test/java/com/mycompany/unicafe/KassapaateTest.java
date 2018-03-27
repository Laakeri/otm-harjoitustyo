package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class KassapaateTest {
    Kassapaate paate;
    
    @Before
    public void setUp() {
        paate = new Kassapaate();
    }
    
    @Test
    public void rahatOikein() {
        assertEquals(100000, paate.kassassaRahaa());
    }
    
    @Test
    public void myytyOikein() {
        assertEquals(0, paate.edullisiaLounaitaMyyty());
        assertEquals(0, paate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void edullinenKateisOsto() {
        assertEquals(10, paate.syoEdullisesti(250));
        assertEquals(100240, paate.kassassaRahaa());
    }
    
    @Test
    public void edullinenMyydytKasvaa() {
        assertEquals(10, paate.syoEdullisesti(250));
        assertEquals(1, paate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void edullinenRahatEiRiita() {
        assertEquals(230, paate.syoEdullisesti(230));
        assertEquals(100000, paate.kassassaRahaa());
        assertEquals(0, paate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void maukasKateisOsto() {
        assertEquals(10, paate.syoMaukkaasti(410));
        assertEquals(100400, paate.kassassaRahaa());
    }
    
    @Test
    public void maukasMyydytKasvaa() {
        assertEquals(10, paate.syoMaukkaasti(410));
        assertEquals(1, paate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void maukasRahatEiRiita() {
        assertEquals(390, paate.syoMaukkaasti(390));
        assertEquals(100000, paate.kassassaRahaa());
        assertEquals(0, paate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void edullinenKorttiOsto() {
        Maksukortti kortti = new Maksukortti(1000);
        assertEquals(true, paate.syoEdullisesti(kortti));
        assertEquals(760, kortti.saldo());
        assertEquals(1, paate.edullisiaLounaitaMyyty());
        assertEquals(100000, paate.kassassaRahaa());
    }
    
    @Test
    public void edullinenKorttiRahatLoppu() {
        Maksukortti kortti = new Maksukortti(230);
        assertEquals(false, paate.syoEdullisesti(kortti));
        assertEquals(0, paate.edullisiaLounaitaMyyty());
        assertEquals(100000, paate.kassassaRahaa());
    }
    
    @Test
    public void maukasKorttiOsto() {
        Maksukortti kortti = new Maksukortti(1000);
        assertEquals(true, paate.syoMaukkaasti(kortti));
        assertEquals(600, kortti.saldo());
        assertEquals(1, paate.maukkaitaLounaitaMyyty());
        assertEquals(100000, paate.kassassaRahaa());
    }
    
    @Test
    public void maukasKorttiRahatLoppu() {
        Maksukortti kortti = new Maksukortti(390);
        assertEquals(false, paate.syoMaukkaasti(kortti));
        assertEquals(0, paate.maukkaitaLounaitaMyyty());
        assertEquals(100000, paate.kassassaRahaa());
    }
    
    @Test
    public void lataaRahaa() {
        Maksukortti kortti = new Maksukortti(1000);
        paate.lataaRahaaKortille(kortti, 500);
        assertEquals(1500, kortti.saldo());
        assertEquals(100500, paate.kassassaRahaa());
    }
    
    @Test
    public void lataaNegatiivinenRahaa() {
        Maksukortti kortti = new Maksukortti(1000);
        paate.lataaRahaaKortille(kortti, -500);
        assertEquals(1000, kortti.saldo());
        assertEquals(100000, paate.kassassaRahaa());
    }
}
