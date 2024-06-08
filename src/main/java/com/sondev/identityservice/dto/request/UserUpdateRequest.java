package com.sondev.identityservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
//Đây là một annotation rút gọn kết hợp các tính năng của @Getter, @Setter,
// @ToString, @EqualsAndHashCode, và @RequiredArgsConstructor.
//Nó tự động tạo ra các phương thức getter và setter cho tất cả các thuộc tính,
// cũng như các phương thức toString(), equals(), và hashCode().
@NoArgsConstructor
// Tạo ra một constructor không có tham số (constructor mặc định).
// Hữu ích để tạo các instance mà không cần truyền vào các giá trị ban đầu.
@AllArgsConstructor
// Tạo ra một constructor với một tham số cho mỗi thuộc tính trong lớp.
// Hữu ích để tạo các instance với tất cả các giá trị cần thiết cùng một lúc.

@Builder
// Cung cấp một builder pattern để tạo đối tượng
// Hữu ích khi bạn muốn tạo một đối tượng với một cách linh hoạt hơn, đặc biệt khi có nhiều thuộc tính.
@FieldDefaults(level = AccessLevel.PRIVATE)
// Đặt mức truy cập mặc định cho tất cả các thuộc tính trong lớp là private.
// Điều này có nghĩa là các thuộc tính chỉ có thể được truy cập thông qua các phương thức getter và setter.
public class UserUpdateRequest {
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob;

}
