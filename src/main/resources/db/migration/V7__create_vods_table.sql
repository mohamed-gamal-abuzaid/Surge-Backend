CREATE TABLE vods (
                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                      stream_id UUID UNIQUE,
                      channel_id UUID NOT NULL,
                      title VARCHAR(255),
                      thumbnail_url VARCHAR(500),
                      video_url VARCHAR(500),
                      duration_seconds INTEGER,
                      view_count BIGINT NOT NULL DEFAULT 0,
                      status VARCHAR(20) NOT NULL DEFAULT 'PROCESSING',
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                      CONSTRAINT fk_vod_stream FOREIGN KEY (stream_id) REFERENCES streams(id) ON DELETE SET NULL,
                      CONSTRAINT fk_vod_channel FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE
);