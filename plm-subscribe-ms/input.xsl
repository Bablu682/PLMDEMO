<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl">
    <xsl:output method="xml" indent="yes"/>


 
<!-- Adding the values to the existing elements value also replace the <Part> by <Item> --> 
<!-- Working fine -->
 <xsl:template match="node()|@*">
     <xsl:copy>
       <xsl:apply-templates select="node()|@*"/>
     </xsl:copy>
 </xsl:template>

<xsl:template match="Part">
  <Item>
    <xsl:apply-templates select="@*|node()"/>
  </Item>
</xsl:template>
<!-- Above code replacing the <part> by <item> -->
<!-- Below code adding the 0000 to the beginning  value of number -->
 <xsl:template match="Number">
 <ItemNumber>     
  <xsl:value-of select="concat('0000', .)"/>
 </ItemNumber>
 </xsl:template>

<!-- Number format arrangement -->
<xsl:template match="Iteration">
 <Iteration> 
   <xsl:value-of select='format-number( . , "##,##,##")' />
<!--  		<xsl:number  value="25000" grouping-size="2" grouping-separator="-"/> -->
 </Iteration>
 </xsl:template>
 


</xsl:stylesheet>