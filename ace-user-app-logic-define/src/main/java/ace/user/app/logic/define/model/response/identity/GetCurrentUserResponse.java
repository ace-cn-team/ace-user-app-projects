package ace.user.app.logic.define.model.response.identity;

import ace.user.base.define.dao.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/30 15:37
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCurrentUserResponse {
    private User user;
}
