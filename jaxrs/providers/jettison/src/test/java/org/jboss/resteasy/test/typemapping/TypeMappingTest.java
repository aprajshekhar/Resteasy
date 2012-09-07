package org.jboss.resteasy.test.typemapping;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.test.EmbeddedContainer;
import org.jboss.resteasy.util.HttpHeaderNames;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

import static org.jboss.resteasy.test.TestPortProvider.*;
import static org.junit.Assert.*;

public class TypeMappingTest
{

   @Test
   public void acceptXMLOnlyRequestNoProducesNoExtension() throws Exception
   {
      requestAndAssert("noproduces", null, "application/xml", "application/xml",true);
   }

   @Test
   public void acceptJSONOnlyRequestNoProducesNoExtension() throws Exception
   {
      requestAndAssert("noproduces", null, "application/json", "application/json",true);
   }

   @Test
   public void acceptNullRequestNoProducesJSONExtension() throws Exception
   {
      requestAndAssert("noproduces", "json", null, "application/json",true);
   }

   @Test
   public void acceptNullRequestNoProducesXMLExtension() throws Exception
   {
      requestAndAssert("noproduces", "xml", null, "application/xml",true);
   }

   @Test
   public void acceptJSONOnlyRequestNoProducesJSONExtension() throws Exception
   {
      requestAndAssert("noproduces", "json", "application/json", "application/json",true);
   }

   @Test
   public void acceptJSONOnlyRequestNoProducesXMLExtension() throws Exception
   {
      requestAndAssert("noproduces", "xml", "application/json", "application/xml",true);
   }

   @Test
   public void acceptJSONAndXMLRequestNoProducesJSONExtension() throws Exception
   {
      requestAndAssert("noproduces", "json", "application/json, application/xml",
              "application/json",true);
   }

   @Test
   public void acceptXMLAndJSONRequestNoProducesJSONExtension() throws Exception
   {
      requestAndAssert("noproduces", "json", "application/xml, application/json",
              "application/json",true);
   }

   @Test
   public void acceptXMLOnlyRequestNoProducesXMLExtension() throws Exception
   {
      requestAndAssert("noproduces", "xml", "application/xml", "application/xml",true);
   }

   @Test
   public void acceptXMLOnlyRequestNoProducesJSONExtension() throws Exception
   {
      requestAndAssert("noproduces", "json", "application/xml", "application/json",true);
   }

   @Test
   public void acceptJSONAndXMLRequestNoProducesXMLExtension() throws Exception
   {
      requestAndAssert("noproduces", "xml", "application/json, application/xml",
              "application/xml",true);
   }

   @Test
   public void acceptXMLAndJSONRequestNoProducesXMLExtension() throws Exception
   {
      requestAndAssert("noproduces", "xml", "application/xml, application/json",
              "application/xml",true);
   }
   @Test
   public void acceptXMLOnlyRequestNoProducesNoExtensionNotAtEnd() throws Exception
   {
      requestAndAssert("noproduces", null, "application/xml", "application/xml",false);
   }

   @Test
   public void acceptJSONOnlyRequestNoProducesNoExtensionNotAtEnd() throws Exception
   {
      requestAndAssert("noproduces", null, "application/json", "application/json",false);
   }

   @Test
   public void acceptNullRequestNoProducesJSONExtensionNotAtEnd() throws Exception
   {
      requestAndAssert("noproduces", "json", null, "application/json",false);
   }

   @Test
   public void acceptNullRequestNoProducesXMLExtensionNotAtEnd() throws Exception
   {
      requestAndAssert("noproduces", "xml", null, "application/xml",false);
   }

   @Test
   public void acceptJSONOnlyRequestNoProducesJSONExtensionNotAtEnd() throws Exception
   {
      requestAndAssert("noproduces", "json", "application/json", "application/json",false);
   }

   @Test
   public void acceptJSONOnlyRequestNoProducesXMLExtensionNotAtEnd() throws Exception
   {
      requestAndAssert("noproduces", "xml", "application/json", "application/xml",false);
   }

   @Test
   public void acceptJSONAndXMLRequestNoProducesJSONExtensionNotAtEnd() throws Exception
   {
      requestAndAssert("noproduces", "json", "application/json, application/xml",
              "application/json",false);
   }

   @Test
   public void acceptXMLAndJSONRequestNoProducesJSONExtensionNotAtEnd() throws Exception
   {
      requestAndAssert("noproduces", "json", "application/xml, application/json",
              "application/json",false);
   }

   @Test
   public void acceptXMLOnlyRequestNoProducesXMLExtensionNotAtEnd() throws Exception
   {
      requestAndAssert("noproduces", "xml", "application/xml", "application/xml",false);
   }

   @Test
   public void acceptXMLOnlyRequestNoProducesJSONExtensionNotAtEnd() throws Exception
   {
      requestAndAssert("noproduces", "json", "application/xml", "application/json",false);
   }

   @Test
   public void acceptJSONAndXMLRequestNoProducesXMLExtensionNotAtEnd() throws Exception
   {
      requestAndAssert("noproduces", "xml", "application/json, application/xml",
              "application/xml",false);
   }

   @Test
   public void acceptXMLAndJSONRequestNoProducesXMLExtensionNotAtEnd() throws Exception
   {
      requestAndAssert("noproduces", "xml", "application/xml, application/json",
              "application/xml",false);
   }

   @Before
   public void startServer() throws Exception
   {
      ResteasyDeployment deployment = new ResteasyDeployment();
      Map<String, String> mediaTypeMappings = new HashMap<String, String>();
      mediaTypeMappings.put("xml", "application/xml");
      mediaTypeMappings.put("json", "application/json");
      deployment.setMediaTypeMappings(mediaTypeMappings);
      EmbeddedContainer.start(deployment);

      deployment.getRegistry().addPerRequestResource(TestResource.class);
   }

   @After
   public void stopServer() throws Exception
   {
      EmbeddedContainer.stop();
   }

   private void requestAndAssert(String path, String extension, String accept,
                                 String expectedContentType, boolean extAtEnd) throws Exception
   {
      String url = null;
      if (extension != null && extAtEnd)
      {
         url = generateURL("/test/" + path);
         url = url + "." + extension;
      }
      else if(extension!=null && !extAtEnd){
         url = "/test"+"."+extension +"/"+path; 
         url = generateURL(url);
      }else if(extension==null && path!=null){
       url = generateURL("/test/"+path);
   }
      ClientRequest request = new ClientRequest(url);
      if (accept != null)
      {
         request.header(HttpHeaderNames.ACCEPT, accept);
      }
      ClientResponse<?> response = request.get();
      assertEquals("Request for " + url + " returned a non-200 status", 200, response.getStatus());
      assertEquals("Request for " + url + " returned an unexpected content type",
              expectedContentType, response.getResponseHeaders().getFirst("Content-type"));
   }

   @XmlRootElement
   public static class TestBean
   {
      private String name;

      public TestBean()
      {

      }

      public TestBean(String name)
      {
         this.name = name;
      }

      public String getName()
      {
         return name;
      }

      public void setName(String name)
      {
         this.name = name;
      }

   }

   @Path("/test")
   public static class TestResource
   {

      @GET
      @Path("/noproduces")
      public TestBean get()
      {
         return new TestBean("name");
      }
   }
}
