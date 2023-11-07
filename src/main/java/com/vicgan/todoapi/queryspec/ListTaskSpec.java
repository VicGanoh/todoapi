package com.vicgan.todoapi.queryspec;

import com.vicgan.todoapi.entities.Task;
import net.kaczmarzyk.spring.data.jpa.domain.EqualIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Spec(path = "completed", params = "completed", spec = EqualIgnoreCase.class)
public interface ListTaskSpec extends Specification<Task> {
}
