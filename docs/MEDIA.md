# Media Streaming

## Overview

Lessons have a media asset (video) that is streamed via HTTP Range requests (206 Partial Content), allowing seeking in the browser `<video>` tag.

## Architecture

```
Browser <video> → GET /api/v1/media/stream?token=...&lessonId=...
                      ↓
              Validate stream token (HMAC, 5 min TTL)
                      ↓
              Load media resource (local storage / classpath)
                      ↓
              Return ResourceRegion (206 Partial Content)
```

## Stream Tokens

- **Format**: Base64(payload).HMAC-SHA256(payload)
- **Payload**: `userId:lessonId:expiryTimestamp`
- **TTL**: 5 minutes (configurable via `app.media.stream-token-ttl`)
- **Bound to**: User ID + Lesson ID (cannot be reused for other lessons)

## Access Control

| User | Access |
|------|--------|
| Admin | All lessons |
| Instructor (owner) | Own lessons |
| Student (enrolled) | Enrolled lessons |
| Anyone | Free preview lessons |

## Media Storage

### LocalMediaStorage (Default)

- Stores files in a configurable directory (`app.media.storage-dir`)
- Falls back to classpath for seed media (`src/main/resources/media/`)
- Path traversal protection

### S3-Compatible (Future)

Implement the `MediaStorage` interface for S3-compatible storage. Render's filesystem is ephemeral, so uploads beyond seed data require external storage.

## Seed Media

4 dummy MP4 files generated with ffmpeg (test patterns + tone):
- `lesson1.mp4` — 20 seconds, ~1.7MB
- `lesson2.mp4` — 20 seconds, ~1.7MB
- `lesson3.mp4` — 20 seconds, ~1.7MB
- `lesson4.mp4` — 20 seconds, ~1.7MB

## Range Requests

The streaming endpoint supports HTTP Range requests:

```
GET /api/v1/media/stream?token=...&lessonId=...
Range: bytes=0-1023
```

Response:
```
HTTP/1.1 206 Partial Content
Content-Type: video/mp4
Content-Range: bytes 0-1023/1787031
Accept-Ranges: bytes
Content-Length: 1024
```

## Security

- Stream tokens are short-lived (5 min)
- Tokens are bound to user + lesson
- Expired tokens are rejected (403)
- Forged tokens are rejected (403)
- Unenrolled users are rejected (403)
