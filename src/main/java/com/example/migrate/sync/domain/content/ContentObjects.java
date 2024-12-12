package com.example.migrate.sync.domain.content;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xinghuimin
 */
@XStreamAlias("ADI")
@Data
public class ContentObjects implements Serializable {

	@XStreamAlias("Objects")
	private List<ContentObject> contentObjectList;

}
