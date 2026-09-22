# Roadmap

This is the source of truth for what to study next.

The roadmap is intentionally flexible. Small experiments may be inserted when a concept needs more depth.

## Phase 1 - Ruby / Web Fundamentals

- [x] 01 CLI Todo
- [x] 02 TCP Echo Server
- [x] 03 HTTP Server
- [x] 04 REST API
- [x] 05 SQLite Todo API
- [x] 06 Login API
- [x] 07 Mini SNS
- [x] 08 Rails Mini SNS

Understand Ruby basics as needed rather than studying syntax separately for a long period.

## Phase 2 - Java / Backend Architecture

- [x] 09 REST API in Java
- [x] 10 Threaded HTTP Server
- [x] 11 Spring Boot API
- [ ] 12 Authentication / Spring Security
- [ ] 13 Java Mini SNS
- [ ] 14 Connection Pool Lab
- [ ] 15 JVM / GC Lab

Focus on static typing, JVM, GC, threads, concurrency, DI, and common backend architecture.

## Phase 3 - Database Internals

- [ ] 16 File KV Store
- [ ] 17 Persistent KV Store
- [ ] 18 Indexed KV Store
- [ ] 19 B-Tree
- [ ] 20 WAL / Crash Recovery
- [ ] 21 Mini SQL Engine

Questions to answer include: why indexes are fast, why WAL exists, what happens on a crash, and how queries become reads/writes.

## Phase 4 - C / Systems

- [ ] 22 Memory Lab
- [ ] 23 Simple Memory Allocator
- [ ] 24 File I/O Lab
- [ ] 25 TCP Server
- [ ] 26 HTTP Server
- [ ] 27 KV Database

Use C to expose pointers, stack/heap, malloc/free, file descriptors, syscalls, sockets, processes, and threads.

## Phase 5 - Search

- [ ] 28 Inverted Index
- [ ] 29 Ranking: TF-IDF / BM25
- [ ] 30 Semantic Search

Connect classic search to embeddings and later AI/RAG systems.

## Phase 6 - Distributed / Real-time

- [ ] 31 WebSocket Chat
- [ ] 32 Redis-like Cache
- [ ] 33 Message Queue
- [ ] 34 Job Queue
- [ ] 35 Rate Limiter
- [ ] 36 Load Balancer
- [ ] 37 Replicated KV Store
- [ ] 38 Consistent Hashing / Sharding
- [ ] 39 Leader Election
- [ ] 40 Failure Lab

Study retries, timeouts, idempotency, replication, partitioning, consistency, and failure.

## Integration Projects

### Mini Discord

Grow it gradually:

REST API -> authentication -> servers/channels -> WebSocket chat -> PostgreSQL -> Redis -> queue/workers -> search -> semantic search/AI -> Docker -> CI/CD.

### Live Streaming Server

Explore streaming protocols, HLS/WebRTC, codecs, chunks/segments, latency, bandwidth, and eventually distribution/CDN concepts.

## Infrastructure

Prefer free/local development first:

- Docker / Docker Compose
- PostgreSQL
- Redis-compatible tools where useful
- GitHub Actions

Multiple services/nodes can initially be simulated with local containers.

## Documentation Site - Later

Keep `docs/` as Markdown source files.

When the notes become large enough, add VitePress (or a similar static documentation tool) so the same Markdown can be browsed as an HTML documentation site with navigation and syntax highlighting.
