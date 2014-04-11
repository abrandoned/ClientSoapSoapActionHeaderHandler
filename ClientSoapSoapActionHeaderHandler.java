package com.jaxws.ext.handlers;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class ClientSoapSoapActionHeaderHandler implements SOAPHandler<SOAPMessageContext> {
  private String actionRoot;

  public ClientSoapSoapActionHeaderHandler() {
    this.actionRoot = "";
  }

  public ClientSoapSoapActionHeaderHandler( String _actionRoot ) {
    this.actionRoot = _actionRoot;
  }

  public void setActionRoot( String _actionRoot ) {
    this.actionRoot = _actionRoot;
  }

  @Override
  public boolean handleMessage(SOAPMessageContext context) {
    Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

    if (outboundProperty.booleanValue()) {
      try {
        final SOAPMessage message = context.getMessage();
        final SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        final SOAPBody body = envelope.getBody();
        Iterator childElements = body.getChildElements();

        if (childElements.hasNext()) {
          Object nextElement = childElements.next();
          SOAPElement element = (SOAPElement) nextElement;

          MimeHeaders headers = message.getMimeHeaders();
          headers.addHeader("SOAPAction", this.actionRoot + element.getElementName().getLocalName());
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return true;
  }

  @Override
  public Set<QName> getHeaders() {
    return new TreeSet();
  }

  @Override
  public boolean handleFault(SOAPMessageContext context) {
    return false;
  }

  @Override
  public void close(MessageContext context) {}
}
