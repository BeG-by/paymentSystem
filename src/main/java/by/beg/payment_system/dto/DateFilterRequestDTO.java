package by.beg.payment_system.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateFilterRequestDTO {

    @NotNull(message = "Date can't be empty")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date firstDate;

    @NotNull(message = "Date can't be empty")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date secondDate;

    public LocalDateTime getFirstDate() {
        return firstDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public LocalDateTime getSecondDate() {
        return secondDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
