package by.logonuk.domain;

import lombok.*;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.sql.Timestamp;
@Setter
@Getter
@EqualsAndHashCode
@Builder
//@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String userName;
    private String surname;
    private Timestamp birth;
    private boolean isDeleted;
    private Timestamp creationDate;
    private Timestamp modificationDate;
    private Double weight;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
