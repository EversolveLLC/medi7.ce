<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <xsl:output method="text"/>
    <xsl:variable name="MAXLEN">23</xsl:variable>
    <xsl:variable name="delim">
        <xsl:value-of select="string('~')"/>
    </xsl:variable>
    <xsl:variable name="namedelim">
        <xsl:value-of select="string('_')"/>
    </xsl:variable>
    <xsl:variable name="equal">
        <xsl:value-of select="string('=')"/>
    </xsl:variable>
    <xsl:variable name="profile_delim">
        <xsl:value-of select="string(':')"/>
    </xsl:variable>
    <xsl:variable name="cr">
        <xsl:text>
</xsl:text>
    </xsl:variable>
    <xsl:template match="/">
        <xsl:apply-templates select="Specification"/>
    </xsl:template>
    <!-- Specification -->
    <xsl:template match="Specification">
        <xsl:param name="prefix"/>
        <xsl:apply-templates select="Message">
            <xsl:with-param name="oid">
                <xsl:value-of select="@HL7OID"/>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>
    <!-- Message  Template-->
    <xsl:template match="Message">
        <xsl:param name="oid"/>
        <!-- Message Definition -->
        <xsl:value-of select="@MsgType"/>
        <xsl:value-of select="$profile_delim"/>
        <xsl:value-of select="@EventType"/>=<xsl:for-each select="child::node()">
            <xsl:if test="(@Name)">
                <xsl:value-of select="@Name"/>(<xsl:if test="name()='Segment'">S</xsl:if>
                <xsl:if test="name()='SegGroup'">SG</xsl:if>
                <xsl:if test="@Repeatable='True' ">R</xsl:if>)<xsl:value-of select="$delim"/>
            </xsl:if>
        </xsl:for-each>
        <xsl:value-of select="$cr"/>
        <!-- Message Profile -->
        <xsl:if test="count(child::Segment[@Optionality='R'])&gt;0">
            <xsl:value-of select="@MsgType"/>
            <xsl:value-of select="$profile_delim"/>
            <xsl:value-of select="@EventType"/>
            <xsl:value-of select="$profile_delim"/>
            <xsl:value-of select="$oid"/>=<xsl:for-each select="child::node()">
                <xsl:if test="(@Name)and(@Optionality='R')">
                    <xsl:value-of select="replace(normalize-space(@Name),' ','')"/>,<xsl:value-of select="replace(normalize-space(@Name),' ','')"/>
                    <xsl:value-of select="$profile_delim"/>
                    <xsl:value-of select="$oid"/>,<xsl:if test="@Optionality='R'">r</xsl:if>
                    <xsl:if test="@Optionality='NS'">x</xsl:if>,<xsl:if test="(@Repeatable='True')and(@Max!='*')">
                        <xsl:value-of select="@Max"/>
                    </xsl:if>
                    <xsl:value-of select="$delim"/>
                </xsl:if>
            </xsl:for-each>
            <xsl:text/>
        </xsl:if>
        <xsl:apply-templates select="Segment">
            <xsl:with-param name="oid">
                <xsl:value-of select="$oid"/>
            </xsl:with-param>
        </xsl:apply-templates>
        <xsl:apply-templates select="SegGroup">
            <xsl:with-param name="oid">
                <xsl:value-of select="$oid"/>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>
    <!-- Segment Group -->
    <xsl:template match="SegGroup">
        <xsl:param name="group"/>
        <xsl:param name="oid"/>
        <xsl:variable name="group_name">
            <xsl:if test="$group!=''">
                <xsl:value-of select="$group"/>
                <xsl:value-of select="$namedelim"/>
            </xsl:if>
            <xsl:value-of select="substring(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(@Name,' ',''),'&amp;',''),'-',''),'/',''),'\[',''),'\]',''),'_','' ),'\)','' ),'\(','' ),&quot;'&quot;,''),1,$MAXLEN)"/>
        </xsl:variable>
        <!-- Group Definition -->
        <xsl:if test="(@Name)">
            <xsl:value-of select="$group_name"/>
            <xsl:value-of select="$equal"/>
            <xsl:for-each select="child::node()">
                <xsl:if test="@Name">
                    <xsl:variable name="seg_name">
                        <xsl:value-of select="$group_name"/>
                        <xsl:value-of select="$namedelim"/>
                        <xsl:value-of select="substring(replace(replace(replace(replace(replace(replace(replace(@Name,' ',''),'&amp;',''),'-',''),'/',''),'\[',''),'\]',''),&quot;'&quot;,''),1,$MAXLEN)"/>
                    </xsl:variable>
                    <xsl:value-of select="$seg_name"/>(<xsl:if test="name()='Segment'">S</xsl:if>
                    <xsl:if test="name()='SegGroup'">SG</xsl:if>
                    <xsl:if test="@Repeatable='True' ">R</xsl:if>)<xsl:value-of select="$delim"/>
                </xsl:if>
            </xsl:for-each>
   <xsl:value-of select="$cr"/>
            <!-- Group Profile -->
            <xsl:if test="count(child::node()[@Optionality='R'])&gt;0">
                <xsl:value-of select="$group_name"/>
                <xsl:value-of select="$profile_delim"/>
                <xsl:value-of select="$oid"/>
                <xsl:value-of select="$equal"/>
                <xsl:for-each select="child::node()">
                    <xsl:if test="(@Name)and(@Optionality='R')">
                        <xsl:variable name="seg_name">
                            <xsl:value-of select="$group_name"/>
                            <xsl:value-of select="$namedelim"/>
                            <xsl:value-of select="substring(replace(replace(replace(replace(replace(replace(replace(@Name,' ',''),'&amp;',''),'-',''),'/',''),'\[',''),'\]',''),&quot;'&quot;,''),1,$MAXLEN)"/>
                        </xsl:variable>
                        <xsl:value-of select="$seg_name"/>,<xsl:value-of select="$seg_name"/>
                        <xsl:value-of select="$profile_delim"/>
                        <xsl:value-of select="$oid"/>,<xsl:if test="@Optionality='R'">r</xsl:if>
                        <xsl:if test="@Optionality='NS'">x</xsl:if>,<xsl:if test="(@Repeatable='True')and(@Max!='*')">
                            <xsl:value-of select="@Max"/>
                        </xsl:if>
                        <xsl:value-of select="$delim"/>
                    </xsl:if>
                </xsl:for-each>
               <xsl:value-of select="$cr"/>
            </xsl:if>
            <!-- ***  -->
            <xsl:apply-templates select="Segment">
                <xsl:with-param name="group">
                    <xsl:value-of select="$group_name"/>
                </xsl:with-param>
                <xsl:with-param name="oid">
                    <xsl:value-of select="$oid"/>
                </xsl:with-param>
            </xsl:apply-templates>
            <xsl:apply-templates select="SegGroup">
                <xsl:with-param name="group">
                    <xsl:value-of select="$group_name"/>
                </xsl:with-param>
                <xsl:with-param name="oid">
                    <xsl:value-of select="$oid"/>
                </xsl:with-param>
            </xsl:apply-templates>
        </xsl:if>
    </xsl:template>
    <!-- ************************  -->
    <!-- * Segment Template  *  -->
    <!-- ************************* -->
    <xsl:template match="Segment">
        <xsl:param name="group"/>
        <xsl:param name="oid"/>
        <xsl:value-of select="$cr"/>
        <!-- Segment Definition -->
        <xsl:variable name="seg_name">
            <xsl:if test="$group!=''">
                <xsl:value-of select="$group"/>
                <xsl:value-of select="$namedelim"/>
            </xsl:if>
            <xsl:value-of select="substring(replace( replace( replace( replace( replace( replace( replace( replace( replace(replace( replace(@Name,' ',''),'&amp;','') ,'-','') ,'/','') ,'\[','') ,'\]',''),'\.','') ,',','') ,'\)','' ),'\(','' ),&quot;'&quot;,'') ,1,$MAXLEN)"/>          
        </xsl:variable>
        <xsl:value-of select="$seg_name"/>
        <xsl:value-of select="$equal"/>
        <xsl:for-each select="child::Field">
            <xsl:if test="@Name">
                <xsl:variable name="field_name">
                    <xsl:value-of select="substring(replace( replace( replace( replace( replace( replace( replace( replace( replace(replace( replace(@Name,' ',''),'&amp;','') ,'-','') ,'/','') ,'\[','') ,'\]',''),'\.','') ,',','') ,'\)','' ),'\(','' ),&quot;'&quot;,'') ,1,$MAXLEN)"/>
                </xsl:variable>
                <xsl:if test="$field_name!='FieldSeparator'">
                    <xsl:variable name="datatype_name">
                        <xsl:value-of select="replace(@Datatype,' ','')"/>
                        <xsl:if test="count(child::Component)&gt;0">
                            <xsl:value-of select="concat($namedelim,$seg_name)"/>
                            <xsl:if test="@Sequence &lt; 10">
                                <xsl:value-of select="string('0')"/>
                            </xsl:if>
                            <xsl:value-of select="normalize-space(@Sequence)"/>
                        </xsl:if>
                    </xsl:variable>
                    <xsl:value-of select="$datatype_name"/>
                    <xsl:value-of select="string('(')"/>
                    <xsl:if test="count(child::Component)&gt;0">
                        <xsl:value-of select="string('C')"/>
                    </xsl:if>
                    <xsl:value-of select="string('F')"/>
                    <xsl:if test="@Repeatable='True'">
                        <xsl:value-of select="string('R')"/>
                    </xsl:if>
                    <xsl:value-of select="string(')')"/>
                    <xsl:value-of select="$field_name"/>
                    <xsl:value-of select="$delim"/>
                </xsl:if>
            </xsl:if>
        </xsl:for-each>
        <xsl:value-of select="$cr"/>
        <!-- Segment Profile -->
        <!-- <xsl:variable name="field_profile_suffix">
            <xsl:value-of select="$seg_name"/>
            <xsl:value-of select="$parent"/>
        </xsl:variable> -->
        <xsl:if test="count(child::Field[@Optionality='R'])&gt;0">
            <xsl:value-of select="$seg_name"/>
            <xsl:value-of select="$profile_delim"/>
            <xsl:value-of select="$oid"/>
            <xsl:value-of select="string('=')"/>
            <xsl:for-each select="child::Field">
                <xsl:variable name="field_name">
                                   <xsl:value-of select="substring(replace( replace( replace( replace( replace( replace( replace( replace( replace( replace(replace(@Name,' ',''),'&amp;','') ,'-','') ,'/','') ,'\[','') ,'\]',''),'\.','') ,',','') ,'\)','' ),'\(','' ) ,&quot;'&quot;,''),1,$MAXLEN)"/>
                </xsl:variable>
                <xsl:if test="$field_name!='FieldSeparator'">
                    <xsl:variable name="datatype_name">
                        <xsl:value-of select="replace(@Datatype,' ','')"/>
                        <xsl:if test="count(child::Component)&gt;0">
                            <xsl:value-of select="concat($namedelim,$seg_name)"/>
                            <xsl:if test="@Sequence &lt; 10">
                                <xsl:value-of select="string('0')"/>
                            </xsl:if>
                            <xsl:value-of select="normalize-space(@Sequence)"/>
                        </xsl:if>
                    </xsl:variable>
                    <xsl:variable name="profile_name">
                        <xsl:value-of select="$datatype_name"/>
                        <xsl:value-of select="$profile_delim"/>
                        <xsl:value-of select="$oid"/>
                    </xsl:variable>
                    <xsl:value-of select="$field_name"/>,<xsl:if test="count(child::Component[@Optionality='R'])&gt;0">
                        <xsl:value-of select="$profile_name"/>
                    </xsl:if>,r,<xsl:if test="(@Repeatable='True')and(@Max!='*')">
                        <xsl:value-of select="@Max"/>
                    </xsl:if>,<xsl:value-of select="DataValues/@Regex"/>
                    <xsl:value-of select="$delim"/>
                </xsl:if>
            </xsl:for-each>
            <xsl:value-of select="$cr"/>
        </xsl:if>
        <!-- Invoke the Field template for composites -->
        <xsl:apply-templates select="Field">
            <xsl:with-param name="segment">
                <xsl:value-of select="$seg_name"/>
            </xsl:with-param>
            <xsl:with-param name="oid">
                <xsl:value-of select="$oid"/>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>
    <!-- Composite Fields -->
    <xsl:template match="Field">
        <xsl:param name="segment"/>
        <xsl:param name="oid"/>
        <xsl:variable name="field_name">
            <xsl:value-of select="substring(replace( replace( replace( replace( replace( replace( replace( replace( replace(replace( replace(@Name,' ',''),'&amp;','') ,'-','') ,'/','') ,'\[','') ,'\]',''),'\.','') ,',','') ,'\)','' ),'\(','' ) ,&quot;'&quot;,''),1,$MAXLEN)"/>
        </xsl:variable>
        <xsl:variable name="datatype_name">
            <xsl:value-of select="replace(@Datatype,' ','')"/>           
            <xsl:if test="count(child::Component)&gt;0">
                 <xsl:value-of select="$namedelim"/>
                <xsl:value-of select="$segment"/>
                <xsl:if test="@Sequence &lt; 10">
                    <xsl:value-of select="string('0')"/>
                </xsl:if>
                <xsl:value-of select="normalize-space(@Sequence)"/>
            </xsl:if>
        </xsl:variable>
        <xsl:variable name="datatype_profile">
            <xsl:value-of select="$datatype_name"/>
            <xsl:value-of select="$profile_delim"/>
            <xsl:value-of select="$oid"/>
        </xsl:variable>
        <xsl:variable name="field_seq">
            <xsl:value-of select="normalize-space(@Sequence)"/>
        </xsl:variable>
        <!-- Datatype definition -->
        <xsl:if test="count(child::Component)&gt;0">
            <xsl:value-of select="$datatype_name"/>
            <xsl:value-of select="$equal"/>
            <xsl:for-each select="child::Component">
                <xsl:if test="@Name">
                    <xsl:variable name="comp_name">
                        <xsl:value-of select="substring(replace( replace( replace( replace( replace( replace( replace( replace( replace( replace(replace(@Name,' ',''),'&amp;','') ,'-','') ,'/','') ,'\[','') ,'\]',''),'\.','') ,',','') ,'\)','' ),'\(','' ),&quot;'&quot;,'') ,1,$MAXLEN)"/>
                    </xsl:variable>
                    <xsl:variable name="comp_seq">
                        <xsl:value-of select="normalize-space(@Sequence)"/>
                    </xsl:variable>
                    <xsl:variable name="innerdatatype_name">
                        <xsl:value-of select="replace(@Datatype,' ','')"/>
                    </xsl:variable>
                    <xsl:variable name="suffix">
                        <xsl:if test="count(child::SubComponent)&gt;0">
                            <xsl:value-of select="concat($namedelim,$segment)"/>
                            <xsl:if test="$field_seq &lt; 10">
                                <xsl:value-of select="string('0')"/>
                            </xsl:if>
                            <xsl:value-of select="$field_seq"/>
                        </xsl:if>
                        <xsl:if test="$comp_seq &lt; 10">
                            <xsl:value-of select="string('0')"/>
                        </xsl:if>
                        <xsl:value-of select="$comp_seq"/>
                    </xsl:variable>
                    <xsl:value-of select="$innerdatatype_name"/>
                    <xsl:if test="count(child::SubComponent)&gt;0">
                        <xsl:value-of select="$suffix"/>
                    </xsl:if>
                    <xsl:value-of select="string('(')"/>
                    <xsl:if test="count(child::SubComponent)&gt;0">
                        <xsl:value-of select="string('C')"/>
                    </xsl:if>
                    <xsl:value-of select="string('F')"/>
                    <xsl:if test="@Repeatable='True'">
                        <xsl:value-of select="string('R')"/>
                    </xsl:if>
                    <xsl:value-of select="string(')')"/>
                    <xsl:value-of select="$comp_name"/>
                    <xsl:value-of select="$delim"/>
                </xsl:if>
            </xsl:for-each>
            <xsl:value-of select="$cr"/>
        </xsl:if>
        <!-- Datatype Profile -->
        <xsl:if test="count(child::Component[@Optionality='R'])&gt;0">
            <xsl:value-of select="$datatype_profile"/>=<xsl:for-each select="child::Component[@Optionality='R']">
                <xsl:variable name="comp_name">
                    <xsl:value-of select="substring(replace(replace( replace( replace( replace( replace( replace( replace( replace( replace( replace(@Name,' ',''),'&amp;','') ,'-','') ,'/','') ,'\[','') ,'\]',''),'\.','') ,',','') ,'\)','' ),'\(','' ),&quot;'&quot;,'') ,1,$MAXLEN)"/>
                </xsl:variable>
                <xsl:variable name="profile_name">
                    <xsl:value-of select="$comp_name"/>
                    <xsl:value-of select="$profile_delim"/>
                    <xsl:value-of select="$segment"/>
                </xsl:variable>
                <xsl:variable name="innerdatatype_name">
                    <xsl:value-of select="replace(@Datatype,' ','')"/>
                </xsl:variable>
                <xsl:variable name="suffix">
                    <xsl:if test="count(child::SubComponent)&gt;0">
                        <xsl:value-of select="concat($namedelim,$segment)"/>
                        <xsl:if test="$field_seq &lt; 10">
                            <xsl:value-of select="string('0')"/>
                        </xsl:if>
                        <xsl:value-of select="normalize-space($field_seq)"/>
                    </xsl:if>
                    <xsl:if test="@Sequence &lt; 10">
                        <xsl:value-of select="string('0')"/>
                    </xsl:if>
                    <xsl:value-of select="normalize-space(@Sequence)"/>
                </xsl:variable>
                <xsl:variable name="profile_name2">
                    <xsl:value-of select="$innerdatatype_name"/>
                    <xsl:value-of select="$suffix"/>
                    <xsl:value-of select="$profile_delim"/>
                    <xsl:value-of select="$oid"/>
                </xsl:variable>
                <xsl:value-of select="$comp_name"/>,<xsl:if  test="count(child::SubComponent[@Optionality='R'])&gt;0">
                    <xsl:value-of select="$profile_name2"/>
                </xsl:if>,r,<xsl:if test="(@Repeatable='True')and(@Max!='*')">
                    <xsl:value-of select="@Max"/>
                </xsl:if>,<xsl:value-of select="DataValues/@Regex"/>
                <xsl:value-of select="$delim"/>
            </xsl:for-each>
            <xsl:value-of select="$cr"/>
        </xsl:if>
        <xsl:apply-templates select="Component">
            <xsl:with-param name="suffix">
                <xsl:value-of select="$segment"/>
                <xsl:if test="@Sequence &lt; 10">
                    <xsl:value-of select="string('0')"/>
                </xsl:if>
                <xsl:value-of select="normalize-space(@Sequence)"/>
            </xsl:with-param>
            <xsl:with-param name="oid">
                <xsl:value-of select="$oid"/>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>
    <!-- Composite Fields -->
    <xsl:template match="Component">
        <xsl:param name="suffix"/>
        <xsl:param name="oid"/>
        <xsl:variable name="suffix2">
            <xsl:value-of select="$suffix"/>
            <xsl:if test="@Sequence &lt; 10">
                <xsl:value-of select="string('0')"/>
            </xsl:if>
            <xsl:value-of select="normalize-space(@Sequence)"/>
        </xsl:variable>
        <xsl:if test="count(child::SubComponent)&gt;0">
            <xsl:variable name="field_name">
                <xsl:value-of select="substring(replace( replace( replace( replace( replace( replace( replace( replace( replace( replace(replace(@Name,' ',''),'&amp;','') ,'-','') ,'/','') ,'\[','') ,'\]',''),'\.','') ,',','') ,'\)','' ),'\(','' ) ,&quot;'&quot;,''),1,$MAXLEN)"/>
            </xsl:variable>
            <xsl:variable name="field_seq">
                <xsl:value-of select="normalize-space(@Sequence)"/>
            </xsl:variable>
            <!-- Subcomponent definition -->
            <xsl:value-of select="@Datatype"/>
            <xsl:value-of select="$namedelim"/>
            <xsl:value-of select="$suffix2"/>
            <xsl:value-of select="string('=')"/>
            <xsl:for-each select="child::SubComponent">
                <xsl:if test="@Name">
                    <xsl:variable name="datatype_suffix">
                        <xsl:value-of select="$suffix2"/>
                        <xsl:if test="@Sequence &lt; 10">
                            <xsl:value-of select="string('0')"/>
                        </xsl:if>
                        <xsl:value-of select="normalize-space(@Sequence)"/>
                    </xsl:variable>
                    <xsl:variable name="comp_name">
                        <xsl:value-of select="substring(replace( replace( replace( replace( replace( replace( replace( replace( replace(replace( replace(@Name,' ',''),'&amp;','') ,'-','') ,'/','') ,'\[','') ,'\]',''),'\.','') ,',','') ,'\)','' ),'\(','' ) ,&quot;'&quot;,''),1,$MAXLEN)"/>
                    </xsl:variable>
                    <xsl:value-of select="@Datatype"/>
                    <xsl:value-of select="string('(')"/>
                    <xsl:value-of select="string('F')"/>
                    <xsl:if test="@Repeatable='True'">
                        <xsl:value-of select="string('R')"/>
                    </xsl:if>
                    <xsl:value-of select="string(')')"/>
                    <xsl:value-of select="$comp_name"/>
                    <xsl:value-of select="$delim"/>
                </xsl:if>
            </xsl:for-each>
            <xsl:value-of select="$cr"/>
            <!-- Subcomponent profile -->
            <xsl:if test="count(child::SubComponent[@Optionality='R'])&gt;0">
                <xsl:value-of select="@Datatype"/>
                <xsl:value-of select="$namedelim"/>
                <xsl:value-of select="$suffix"/>
                <xsl:value-of select="$profile_delim"/>
                <xsl:value-of select="$oid"/>
                <xsl:value-of select="string('=')"/>
                <xsl:for-each select="child::SubComponent[@Optionality='R']">
                    <xsl:variable name="comp_name">
                        <xsl:value-of select="substring(replace( replace( replace( replace( replace( replace( replace( replace( replace(replace( replace(@Name,' ',''),'&amp;','') ,'-','') ,'/','') ,'\[','') ,'\]',''),'\.','') ,',','') ,'\)','' ),'\(','' ),&quot;'&quot;,'') ,1,$MAXLEN)"/>
                    </xsl:variable>
                    <xsl:value-of select="$comp_name"/>, ,r,<xsl:if test="(@Repeatable='True')and(@Max!='*')">
                        <xsl:value-of select="@Max"/>
                    </xsl:if>,<xsl:value-of select="DataValues/@Regex"/>
                    <xsl:value-of select="$delim"/>
                </xsl:for-each>
                <xsl:value-of select="$cr"/>
            </xsl:if>
        </xsl:if>
    </xsl:template>
    <!-- template -->
</xsl:stylesheet>
