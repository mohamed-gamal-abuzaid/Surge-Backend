CREATE TABLE channels (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          user_id UUID NOT NULL UNIQUE,
                          channel_name VARCHAR(50) NOT NULL UNIQUE,
                          title VARCHAR(255),
                          description TEXT,
                          banner_url VARCHAR(500),
                          stream_key UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
                          is_live BOOLEAN NOT NULL DEFAULT FALSE,
                          viewer_count INTEGER NOT NULL DEFAULT 0,
                          language VARCHAR(10) DEFAULT 'ar',
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_channel_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);