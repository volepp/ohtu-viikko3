package ohtu;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import ohtu.verkkokauppa.*;

public class KauppaTest {

	Pankki pankki;
	Viitegeneraattori viite;
	Varasto varasto;
	Kauppa kauppa;
	
	@Before
	public void setUp() {
		pankki = mock(Pankki.class);
		viite = mock(Viitegeneraattori.class);
		varasto = mock(Varasto.class);
		kauppa = new Kauppa(varasto, pankki, viite);
	}
	
	@Test
	public void ostoksenPaaytyttyaPankinMetodiaTilisiirtoKutsutaan() {
	    when(viite.uusi()).thenReturn(42);
	    
	    when(varasto.saldo(1)).thenReturn(10); 
	    when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));            

	    // tehdään ostokset
	    kauppa.aloitaAsiointi();
	    kauppa.lisaaKoriin(1);     // ostetaan tuotetta numero 1 eli maitoa
	    kauppa.tilimaksu("pekka", "12345");

	    // sitten suoritetaan varmistus, että pankin metodia tilisiirto on kutsuttu
	    verify(pankki).tilisiirto(eq("pekka"), eq(42), eq("12345"), eq("33333-44455"),eq(5));   
	    // toistaiseksi ei välitetty kutsussa käytetyistä parametreista
	}
	
	@Test
	public void kaksiEriOstostaOstaessaMetodiaTilisiirtoKutsutaan() {
		when(viite.uusi()).thenReturn(42);
	    
	    when(varasto.saldo(1)).thenReturn(10); 
	    when(varasto.saldo(2)).thenReturn(10); 
	    when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5)); 
	    when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "leipä", 3)); 
	    
		kauppa.aloitaAsiointi();
		kauppa.lisaaKoriin(1);
		kauppa.lisaaKoriin(2);
		kauppa.tilimaksu("pekka", "12345");
		
		verify(pankki).tilisiirto(eq("pekka"), eq(42), eq("12345"), eq("33333-44455"),eq(8));
	}
	
	@Test
	public void kaksiSamaaOstostaOstaessaMetodiaTilisiirtoKutsutaan() {
		when(viite.uusi()).thenReturn(42);
	    
	    when(varasto.saldo(1)).thenReturn(10); 
	    when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));  
	    
		kauppa.aloitaAsiointi();
		kauppa.lisaaKoriin(1);
		kauppa.lisaaKoriin(1);
		kauppa.tilimaksu("pekka", "12345");
		
		verify(pankki).tilisiirto(eq("pekka"), eq(42), eq("12345"), eq("33333-44455"),eq(10));
	}
	
	@Test
	public void tuoteJotaEiOleVarastossaEiVaikutaSummaan() {
		when(viite.uusi()).thenReturn(42);
	    
	    when(varasto.saldo(1)).thenReturn(10); 
	    when(varasto.saldo(2)).thenReturn(0); 
	    when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5)); 
	    when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "leipä", 3)); 
	    
		kauppa.aloitaAsiointi();
		kauppa.lisaaKoriin(1);
		kauppa.lisaaKoriin(2);
		kauppa.tilimaksu("pekka", "12345");
		
		verify(pankki).tilisiirto(eq("pekka"), eq(42), eq("12345"), eq("33333-44455"),eq(5));
	}
	
	@Test
	public void ostostenAloittaminenNollaaEdellisenOstoksen() {
		when(viite.uusi()).thenReturn(42);
	    
	    when(varasto.saldo(1)).thenReturn(10); 
	    when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5)); 
	    
	    kauppa.aloitaAsiointi();
	    kauppa.lisaaKoriin(1);
	    kauppa.tilimaksu("pekka", "12345");
	    
	    kauppa.aloitaAsiointi();
	    kauppa.lisaaKoriin(1);
	    kauppa.tilimaksu("pekka", "12345");
	    
	    verify(pankki, times(2)).tilisiirto(eq("pekka"), eq(42), eq("12345"), eq("33333-44455"),eq(5));
	}
	
	@Test
	public void jokaiselleMaksutapahtumalleOmaViite() {
		when(viite.uusi()).thenReturn(42);
	    
	    when(varasto.saldo(1)).thenReturn(10); 
	    when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5)); 
	    
	    kauppa.aloitaAsiointi();
	    kauppa.lisaaKoriin(1);
	    kauppa.tilimaksu("pekka", "12345");
	    
	    when(viite.uusi()).thenReturn(24);
	    
	    kauppa.aloitaAsiointi();
	    kauppa.lisaaKoriin(1);
	    kauppa.tilimaksu("pekka", "12345");
	    
	    verify(pankki, times(1)).tilisiirto(eq("pekka"), eq(42), eq("12345"), eq("33333-44455"),eq(5));
	    verify(pankki, times(1)).tilisiirto(eq("pekka"), eq(24), eq("12345"), eq("33333-44455"),eq(5));
	}
	
	@Test
	public void koristaPoistaminenToimiiOikein() {
		when(viite.uusi()).thenReturn(42);
	    
		Tuote t = new Tuote(1, "maito", 5);
		
	    when(varasto.saldo(1)).thenReturn(1); 
	    when(varasto.haeTuote(1)).thenReturn(t); 
	    
	    kauppa.aloitaAsiointi();
	    kauppa.lisaaKoriin(1);
	    kauppa.poistaKorista(1);
	    
	    verify(varasto).palautaVarastoon(eq(t));
	}
}
