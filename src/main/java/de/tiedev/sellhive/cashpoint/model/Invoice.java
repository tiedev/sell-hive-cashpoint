package de.tiedev.sellhive.cashpoint.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Invoice {
	
	@Id
	@SequenceGenerator(name="INVOICE_SEQ", sequenceName="invoice_id_seq")
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="INVOICE_SEQ")
	Long id;

	@OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
	List<InvoiceLineItem> lineItems;
	
    public List<InvoiceLineItem> getLineItems()
	{
        return lineItems;
    }
	
	public void setLineItems(List<InvoiceLineItem> lineItems)
	{
		this.lineItems = lineItems;
	}

	public void addLineItem(InvoiceLineItem invoiceLineItem) {
		invoiceLineItem.setInvoice(this);
		if (lineItems == null) {
			lineItems = new ArrayList<InvoiceLineItem>();
		}
		invoiceLineItem.setInvoice(this);
		lineItems.add(invoiceLineItem);
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}

	public boolean containsLineItem(InvoiceLineItem lineItem) {
		if (lineItems == null) return false;
		return lineItems.contains(lineItem);
	}
}
