package com.example.migrate.sync.domain.content;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xinghuimin
 */
@XStreamAlias("Object")
@Data
public class ProgramProperty implements Serializable {

	@XStreamAlias("Action")
	@XStreamAsAttribute
	private String action;

	@XStreamAlias("ElementType")
	@XStreamAsAttribute
	private String elementType;

	@XStreamAlias("ID")
	@XStreamAsAttribute
	private String id;

	@XStreamAlias("Code")
	@XStreamAsAttribute
	private String code;

	@XStreamImplicit(itemFieldName="Property")
	private List<Program> programs;

	@XStreamAlias("Property")
	@XStreamConverter(value= ToAttributedValueConverter.class, strings={"value"})
	@Data
	static class Program implements Serializable {

		private String value;

		@XStreamAlias("Name")
		@XStreamAsAttribute
		private String name;

	}
}
