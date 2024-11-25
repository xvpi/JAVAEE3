CREATE TABLE login_history (
                               id INT AUTO_INCREMENT PRIMARY KEY,      -- 主键，自增ID
                               user_id INT NOT NULL,                   -- 用户ID（关联用户表）
                               username VARCHAR(50) NOT NULL,          -- 用户名（冗余存储，方便展示）
                               login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 登录时间
                               logout_time TIMESTAMP DEFAULT '0000-00-00 00:00:00', -- 登出时间
                               ip_address VARCHAR(45),                 -- 登录IP地址（IPv4/IPv6）
                               device_info VARCHAR(255) DEFAULT NULL   -- 设备信息（浏览器、系统等）
);
delete from login_history;