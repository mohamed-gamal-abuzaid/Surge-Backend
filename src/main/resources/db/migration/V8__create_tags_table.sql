CREATE TABLE tags (
                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                      name VARCHAR(50) NOT NULL UNIQUE,
                      slug VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE stream_tags (
                             stream_id UUID NOT NULL,
                             tag_id UUID NOT NULL,

                             PRIMARY KEY (stream_id, tag_id),
                             CONSTRAINT fk_streamtag_stream FOREIGN KEY (stream_id) REFERENCES streams(id) ON DELETE CASCADE,
                             CONSTRAINT fk_streamtag_tag FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);