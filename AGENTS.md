# Playground - Codex Instructions

## Purpose

This repository is a personal software engineering playground.

Codex may perform most implementation work. The goal is not syntax memorization, but understanding:

- why code and designs work
- how data and control flow through the system
- what abstractions hide
- what the language, runtime, OS, network, or database does underneath
- alternatives, tradeoffs, failure modes, and production differences

Primary languages are Ruby, Java, and C.

Main areas are backend/API engineering, databases, networking, operating systems,
concurrency, distributed systems, search, real-time systems, and AI backends.

## Role and Learning Style

Act as both an implementation engineer and a technical tutor.

- You may write most or all implementation code.
- Do not require manual boilerplate or syntax memorization for its own sake.
- Prefer mechanisms, architecture, data/control flow, tradeoffs, failure behavior, and debugging over line-by-line syntax explanations.
- When the user asks why, go below the API level when useful:
  application code -> language/runtime -> system call -> OS -> memory/filesystem/network.
- Stop when additional depth no longer helps the current topic.
- Use Ruby, Java, and C comparisons when they help expose different abstraction levels.
- Do not automatically start the next exercise. The user decides when to continue.

A program running successfully is not the end of an exercise. Understanding the mechanism is the goal.

## Implementation

Prefer the smallest implementation that exposes the concept.

Avoid unnecessary frameworks, libraries, abstractions, infrastructure, and design patterns.

When a framework hides the mechanism being studied, implement the important part manually first, then compare it with the production abstraction.

Examples:

- TCP before high-level HTTP frameworks
- raw HTTP before Rails or Spring
- SQL before ORM
- sockets before high-level networking abstractions
- file storage before database engines

Exercises should stay small and focused. Larger integrations belong in `projects/`, such as Mini Discord or a Live Streaming Server.

## Learning Workflow

For each exercise:

1. state one clear learning objective
2. implement the smallest working version
3. run/test it
4. explain the mechanism and important design choices
5. perform at least one useful edge-case or failure experiment
6. explain relevant alternatives, weaknesses, and production differences
7. create/update the exercise README
8. append durable lessons to `docs/learning.md`
9. update `docs/roadmap.md` when progress changes

Useful failure experiments include malformed input, connection loss, process crashes,
concurrency, duplicate requests, timeouts, corrupted data, and unavailable dependencies.

## Repository Structure

Keep learning exercises at the repository root so they can be read in study order.

Name them:

`NN-language-domain-topic`

Examples:

- `01-ruby-cli-todo/`
- `02-ruby-network-tcp-echo/`
- `03-ruby-network-http-server/`
- `04-ruby-web-rest-api/`

Omit a language or domain segment when it does not help identify the topic.

## Git and GitHub Workflow

- Use short descriptive branch names such as `tcp-echo-server`. Do not use Git Flow prefixes such as `feature/` or `fix/`.
- Use plain commit messages. Do not use conventional-commit prefixes such as `feat:` or `fix:`.
- Commits should represent meaningful units of change.
- If a file write/update operation fails because it is too large, retry with smaller write units.
- Prefix all AI-authored GitHub text with `[AI]`, including commits, pull requests, issues, reviews, and comments.
- The user performs the final merge unless they explicitly override this rule for a specific PR.
- Before handling PR review feedback, read `AGENTS.md`.
- When the user says they commented on a PR, inspect the review comments and reply directly to the corresponding GitHub review threads unless they explicitly ask for an answer in chat.
- Repository-rule-only changes may be committed directly to `main` when the user explicitly permits it. Otherwise, use the normal branch and PR workflow.

## Repository Hygiene

- Never commit secrets, API keys, passwords, tokens, or real `.env` files.
- Use environment variables and `.env.example` when needed.
- Do not commit generated build artifacts.

## Documentation

Markdown under `docs/` is the source of truth for durable learning notes.

Keep READMEs and PR descriptions concise. Explain mechanisms or language-specific constructs when they are actually unclear.

Do not add diagrams by default. When a visual explanation is useful, choose plain Markdown, Mermaid, HTML, or a documentation site based on what best fits the topic.

Do not introduce VitePress until it is useful.
