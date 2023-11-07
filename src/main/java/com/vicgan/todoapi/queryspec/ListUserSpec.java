package com.vicgan.todoapi.queryspec;

import com.vicgan.todoapi.entities.User;
import net.kaczmarzyk.spring.data.jpa.domain.EqualIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@And({
        @Spec(path = "firstName", params = "first_name", spec= LikeIgnoreCase.class),
        @Spec(path = "lastName", params = "last_name", spec = LikeIgnoreCase.class),
        @Spec(path = "email", params = "email", spec= EqualIgnoreCase.class)
})
public interface ListUserSpec extends Specification<User> {
}
