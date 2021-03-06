package client.net.sf.saxon.ce.expr.instruct;
import client.net.sf.saxon.ce.Configuration;
import client.net.sf.saxon.ce.expr.*;
import client.net.sf.saxon.ce.functions.SystemFunction;
import client.net.sf.saxon.ce.om.NamePool;
import client.net.sf.saxon.ce.om.StandardNames;
import client.net.sf.saxon.ce.pattern.NodeKindTest;
import client.net.sf.saxon.ce.trans.XPathException;
import client.net.sf.saxon.ce.type.ItemType;
import client.net.sf.saxon.ce.type.TypeHierarchy;

/**
 * An instruction derived from an xsl:attribute element in stylesheet, or from
 * an attribute constructor in XQuery. This version deals only with attributes
 * whose name is known at compile time. It is also used for attributes of
 * literal result elements. The value of the attribute is in general computed
 * at run-time.
*/

public final class FixedAttribute extends AttributeCreator {

    private int nameCode;

    /**
     * Construct an Attribute instruction
     * @param nameCode Represents the attribute name
     * of the instruction - zero if the attribute was not present
    */

    public FixedAttribute (  int nameCode) {
        this.nameCode = nameCode;
        setAnnotation(StandardNames.XS_UNTYPED_ATOMIC);
        setOptions(0);
    }

    /**
     * Get the name of this instruction (return 'xsl:attribute')
     */

    public int getInstructionNameCode() {
        return StandardNames.XSL_ATTRIBUTE;
    }

    /**
     * Set the expression defining the value of the attribute. If this is a constant, and if
     * validation against a schema type was requested, the validation is done immediately.
     * @param select The expression defining the content of the attribute
     * @param config The Saxon configuration
     * @throws XPathException if the expression is a constant, and validation is requested, and
     * the constant doesn't match the required type.
     */
    public void setSelect(Expression select, Configuration config) throws XPathException {
        super.setSelect(select, config);
        // If attribute name is xml:id, add whitespace normalization
        if ((nameCode & NamePool.FP_MASK) == StandardNames.XML_ID) {
            select = SystemFunction.makeSystemFunction("normalize-space", new Expression[]{select});
            super.setSelect(select, config);
        }
    }

    public void localTypeCheck(ExpressionVisitor visitor, ItemType contextItemType) throws XPathException {
        //
    }

    /**
     * Get the name pool name code of the attribute to be constructed
     * @return the attribute's name code
     */

    public int getAttributeNameCode() {
        return nameCode;
    }

    public ItemType getItemType(TypeHierarchy th) {
        return NodeKindTest.ATTRIBUTE;
    }

    public int getCardinality() {
        return StaticProperty.EXACTLY_ONE;
    }

    public int evaluateNameCode(XPathContext context)  {
        return nameCode;
    }

}

// This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. 
// If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
// This Source Code Form is “Incompatible With Secondary Licenses”, as defined by the Mozilla Public License, v. 2.0.
