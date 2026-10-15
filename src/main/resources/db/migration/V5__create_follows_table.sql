CREATE TABLE follows (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         follower_id UUID NOT NULL,
                         followed_channel_id UUID NOT NULL,
                         notifications_enabled BOOLEAN NOT NULL DEFAULT TRUE,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT fk_follow_user FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE,
                         CONSTRAINT fk_follow_channel FOREIGN KEY (followed_channel_id) REFERENCES channels(id) ON DELETE CASCADE,
                         CONSTRAINT unique_follow UNIQUE (follower_id, followed_channel_id)
);