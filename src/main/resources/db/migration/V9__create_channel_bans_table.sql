CREATE TABLE channel_bans (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              channel_id UUID NOT NULL,
                              user_id UUID NOT NULL,
                              banned_by UUID NOT NULL,
                              reason VARCHAR(500),
                              type VARCHAR(20) NOT NULL DEFAULT 'PERMANENT',
                              expires_at TIMESTAMP,
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT fk_ban_channel FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE,
                              CONSTRAINT fk_ban_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                              CONSTRAINT fk_ban_by FOREIGN KEY (banned_by) REFERENCES users(id) ON DELETE CASCADE,
                              CONSTRAINT unique_channel_ban UNIQUE (channel_id, user_id)
);