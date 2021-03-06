package edu.uoc.som.jsonschematouml.generators.test;

import junit.framework.TestCase;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Class;
import org.junit.Test;

import edu.uoc.som.jsonschematouml.generators.JSONSchemaToUML;

import java.io.File;

public class JSONSchemaToUMLTest extends TestCase {
    
    @Test
    public void testLaunchAndSave() {
        JSONSchemaToUML io = new JSONSchemaToUML("test");
        File input = new File("inputs/fiware");
        io.launch(input);
        io.saveModel(new File("outputs/model.uml"));
    }

    @Test
    public void testNumericInstance() {
    	JSONSchemaToUML io = new JSONSchemaToUML("test");
        File input = new File("inputs/testNumericInstance.json");
        io.launch(input);
        Model model = io.getModel();

        Class c = (Class) model.getPackagedElement("numericInstance");
        assertNotNull(c);
        assertTrue(c.getOwnedRules().size() > 0);
        for(Constraint constraint : c.getOwnedRules()) {
            assertNotNull(constraint.getSpecification());
            OpaqueExpression spec = (OpaqueExpression) constraint.getSpecification();
            assertEquals("OCL", spec.getLanguages().get(0));
            if(constraint.getName().equals("numericInstance-att1-multipleOfConstraint"))
                assertEquals("self.att1.div(2) = 0", spec.getBodies().get(0));
            if(constraint.getName().equals("numericInstance-att1-maximumConstraint"))
                assertEquals("self.att1 <= 10", spec.getBodies().get(0));
            if(constraint.getName().equals("numericInstance-att1-minimumConstraint"))
                assertEquals("self.att1 >= 0", spec.getBodies().get(0));
            if(constraint.getName().equals("numericInstance-att2-exclusiveMaximumConstraint"))
                assertEquals("self.att2 < 10", spec.getBodies().get(0));
            if(constraint.getName().equals("numericInstance-att2-exclusiveMinimumConstraint"))
                assertEquals("self.att2 > 0", spec.getBodies().get(0));
        }
    }

    @Test
    public void testString() {
    	JSONSchemaToUML io = new JSONSchemaToUML("test");
        File input = new File("inputs/testString.json");
        io.launch(input);
        Model model = io.getModel();

        Class c = (Class) model.getPackagedElement("stringInstance");
        assertNotNull(c);
        assertTrue(c.getOwnedRules().size() > 0);
        for(Constraint constraint : c.getOwnedRules()) {
            assertNotNull(constraint.getSpecification());
            OpaqueExpression spec = (OpaqueExpression) constraint.getSpecification();
            assertEquals("OCL", spec.getLanguages().get(0));
            if(constraint.getName().equals("stringInstance-att1-maxLengthConstraint"))
                assertEquals("self.att1.size() <= 0", spec.getBodies().get(0));
            if(constraint.getName().equals("stringInstance-att1-minLengthConstraint"))
                assertEquals("self.att1.size() >= 2", spec.getBodies().get(0));
        }
    }
    @Test
    public void testAny() {
    	JSONSchemaToUML io = new JSONSchemaToUML("test");
        File input = new File("inputs/testAny.json");
        io.launch(input);
        Model model = io.getModel();

        Class c = (Class) model.getPackagedElement("anyInstance");
        assertNotNull(c);
        assertTrue(c.getOwnedAttributes().size() > 0);
        for(Property property : c.getOwnedAttributes()) {
            if(property.getName().equals("att1")) {
                assertEquals("Boolean", property.getType().getName());
            } else if(property.getName().equals("att2")) {
                assertEquals("Att2", property.getType().getName());
            } else if(property.getName().equals("att3")) {
                // TODO
            } else if(property.getName().equals("att4")) {
                assertEquals("Integer", property.getType().getName());
            } else if(property.getName().equals("att5")) {
                assertEquals("Integer", property.getType().getName());
            } else if(property.getName().equals("att6")) {
                assertEquals("att6Enum", property.getType().getName());
            } else if(property.getName().equals("att7")) {
                assertEquals("String", property.getType().getName());
            }
        }

        Enumeration e = (Enumeration) model.getPackagedElement("att6Enum");
        assertNotNull(e);
        assertNotNull(e.getOwnedLiteral("val1"));
        assertNotNull(e.getOwnedLiteral("val2"));
        assertNotNull(e.getOwnedLiteral("val3"));
    }

    @Test
    public void testObject() {
    	JSONSchemaToUML io = new JSONSchemaToUML("test");
        File input = new File("inputs/testObject.json");
        io.launch(input);
        Model model = io.getModel();

        Class c = (Class) model.getPackagedElement("objectInstance");
        assertNotNull(c);
        assertTrue(c.getOwnedAttributes().size() > 0);
        for(Property property : c.getOwnedAttributes()) {
            if(property.getName().equals("att1")) {
                assertEquals("String", property.getType().getName());
                assertEquals(1, property.getLower());
            }
        }
    }
    
    @Test
    public void testTitleDescription() {
    	JSONSchemaToUML io = new JSONSchemaToUML("test");
        File input = new File("inputs/testTitleDescription.json");
        io.launch(input);
        Model model = io.getModel();
        io.saveModel(new File("outputs/model.uml"));

        Class c = (Class) model.getPackagedElement("titleDescription");
        assertNotNull(c);
        assertTrue(c.getOwnedComments().size() > 0);
        for(Comment comment : c.getOwnedComments()) {
        	if(comment.getBody().startsWith("Title:")) {
                assertEquals("Title: titleTest", comment.getBody());
        	}
        	if(comment.getBody().startsWith("Description:")) {
                assertEquals("Description: descriptionTest", comment.getBody());
        	}
        }
    }
    
    @Test
    public void testSet() {
        JSONSchemaToUML io = new JSONSchemaToUML("test");
        File input = new File("inputs/testSet");
        io.launch(input);
        io.saveModel(new File("outputs/model.uml"));
    }
    
    @Test
    public void testWrong() {
        JSONSchemaToUML io = new JSONSchemaToUML("test");
        File input = new File("inputs/testWrong.json");
        io.launch(input);
        io.saveModel(new File("outputs/model.uml"));
    }
}
