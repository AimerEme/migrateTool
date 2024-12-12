package com.example.migrate.sync.domain.content;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xinghuimin
 */
@XStreamAlias("Objects")
@Data
public class ContentObject implements Serializable {

	@XStreamImplicit(itemFieldName="Object")
	private List<ProgramProperty> programProperty;

}
