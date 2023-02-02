package indexation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import static org.mockito.Mockito.*;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import core.Document;


@ExtendWith(MockitoExtension.class)
class ParserCISITest {
	
	private final String str1 = ".I 2\n"
			+ ".T \n"
			+ "Use Made of Technical Libraries\n"
			+ ".A \n"
			+ "Slater, M.\n"
			+ ".W\n"
			+ "This report is an analysis of 6300 acts of use\n"
			+ "in 104 technical libraries in the United Kingdom.\n"
			+ "Library use is only one aspect of the wider pattern of\n"
			+ "information use.  Information transfer in libraries is\n"
			+ "restricted to the use of documents.  It takes no\n"
			+ "account of documents used outside the library, still\n"
			+ "less of information transferred orally from person\n"
			+ "to person.  The library acts as a channel in only a\n"
			+ "proportion of the situations in which information is\n"
			+ "transferred.\n"
			+ "Taking technical information transfer as a whole,\n"
			+ "there is no doubt that this proportion is not the\n"
			+ "major one.  There are users of technical information -\n"
			+ "particularly in technology rather than science -\n"
			+ "who visit libraries rarely if at all, relying on desk\n"
			+ "collections of handbooks, current periodicals and personal\n"
			+ "contact with their colleagues and with people in other\n"
			+ "organizations.  Even regular library users also receive\n"
			+ "information in other ways.\n"
			+ ".X\n"
			+ "2	5	2\n"
			+ "32	1	2\n"
			+ "76	1	2\n"
			+ "132	1	2\n"
			+ "137	1	2\n"
			+ "139	1	2\n"
			+ "152	2	2\n"
			+ "155	1	2\n"
			+ "158	1	2\n"
			+ "183	1	2\n"
			+ "195	1	2\n"
			+ "203	1	2\n"
			+ "204	1	2\n"
			+ "210	1	2\n"
			+ "243	1	2\n"
			+ "371	1	2\n"
			+ "475	1	2\n"
			+ "552	1	2\n"
			+ "760	1	2\n"
			+ "770	1	2\n"
			+ "771	1	2\n"
			+ "774	1	2\n"
			+ "775	1	2\n"
			+ "776	1	2\n"
			+ "788	1	2\n"
			+ "789	1	2\n"
			+ "801	1	2\n"
			+ "815	1	2\n"
			+ "839	1	2\n"
			+ "977	1	2\n"
			+ "1055	1	2\n"
			+ "1056	1	2\n"
			+ "1151	1	2\n"
			+ "1361	1	2\n"
			+ "1414	1	2\n"
			+ "1451	1	2\n"
			+ "1451	1	2";
	
	private final String str2 = ".T \n"
			+ "Use Made of Technical Libraries\n"
			+ ".A \n"
			+ "Slater, M.\n"
			+ ".W\n"
			+ "This report is an analysis of 6300 acts of use\n"
			+ "in 104 technical libraries in the United Kingdom.\n"
			+ "Library use is only one aspect of the wider pattern of\n"
			+ "information use.  Information transfer in libraries is\n"
			+ "restricted to the use of documents.  It takes no\n"
			+ "account of documents used outside the library, still\n"
			+ "less of information transferred orally from person\n"
			+ "to person.  The library acts as a channel in only a\n"
			+ "proportion of the situations in which information is\n"
			+ "transferred.\n"
			+ "Taking technical information transfer as a whole,\n"
			+ "there is no doubt that this proportion is not the\n"
			+ "major one.  There are users of technical information -\n"
			+ "particularly in technology rather than science -\n"
			+ "who visit libraries rarely if at all, relying on desk\n"
			+ "collections of handbooks, current periodicals and personal\n"
			+ "contact with their colleagues and with people in other\n"
			+ "organizations.  Even regular library users also receive\n"
			+ "information in other ways.\n"
			+ ".X\n"
			+ "2	5	2\n"
			+ "32	1	2\n"
			+ "76	1	2\n"
			+ "132	1	2\n"
			+ "137	1	2\n"
			+ "139	1	2\n"
			+ "152	2	2\n"
			+ "155	1	2\n"
			+ "158	1	2\n"
			+ "183	1	2\n"
			+ "195	1	2\n"
			+ "203	1	2\n"
			+ "204	1	2\n"
			+ "210	1	2\n"
			+ "243	1	2\n"
			+ "371	1	2\n"
			+ "475	1	2\n"
			+ "552	1	2\n"
			+ "760	1	2\n"
			+ "770	1	2\n"
			+ "771	1	2\n"
			+ "774	1	2\n"
			+ "775	1	2\n"
			+ "776	1	2\n"
			+ "788	1	2\n"
			+ "789	1	2\n"
			+ "801	1	2\n"
			+ "815	1	2\n"
			+ "839	1	2\n"
			+ "977	1	2\n"
			+ "1055	1	2\n"
			+ "1056	1	2\n"
			+ "1151	1	2\n"
			+ "1361	1	2\n"
			+ "1414	1	2\n"
			+ "1451	1	2\n"
			+ "1451	1	2";
	
	
	@Spy
	ParserCISI parserSpied; 
	
	@BeforeEach
    public void setUp() {
        //MockitoAnnotations.openMocks(this);
		parserSpied = new ParserCISI(); 
    }
	
	@Test
	void getDocumentTest() {
		try {
			Document doc=parserSpied.getDocument(str1);
			assertEquals(doc.getId(),"2");
		}
		catch(InvalidFormatDocumentException e) {
			fail(e);
		}
	}
	
	@Test
	void getDocumentInvalidTest() {
		assertThrows( InvalidFormatDocumentException.class, () -> {
			parserSpied.getDocument(str2);
		});
	}
	
	
	@Test
	void nextDocumentTest() {
		try {
			parserSpied.init("data/cisi/cisi.txt");
			Document doc=parserSpied.nextDocument();
			assertEquals(doc.getId(),"1");
			doc=parserSpied.nextDocument();
			assertEquals(doc.getId(),"2");
		}
		catch(NonInitializedParserException e) {
			fail(e);
		}
	}
	
	@Test
	@Disabled
	void nextDocumentWithSpyTest() {
		try {
			parserSpied.init("data/cisi/cisi.txt");
			Document doc=new Document("1","a");
			Document doc2=new Document("2","b");
			//Mockito.doReturn(doc).
			when(parserSpied.getDocument(any(String.class))).thenReturn(doc).thenReturn(doc2);
			doc=parserSpied.nextDocument();
			assertEquals(doc.getId(),"1");
			doc=parserSpied.nextDocument();
			assertEquals(doc.getId(),"2");
		}
		catch(NonInitializedParserException e) {
			fail(e);
		} catch (InvalidFormatDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e);
		}
	}
	
	@Test
	void nextDocumentNonInitTest() {
		assertThrows( NonInitializedParserException.class, () -> {
			parserSpied.nextDocument();
		});
	}
	
}
