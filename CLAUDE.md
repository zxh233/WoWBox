# Claude 规范

本文件给 Claude 使用。处理本仓库时遵守下面的规则。

## 硬性规则：`app/src/main/assets/`

严禁修改此目录下未被 git 追踪的文件。

未被追踪，是指该路径不在 `git ls-files` 的结果中。`.gitignore` 忽略的文件同样算未追踪。当前忽略规则是：

- `app/src/main/assets/**/*.tzst`
- `app/src/main/assets/*.tzst`
- `app/src/main/assets/*.txz`

这些是本地运行时包（Wine、imagefs、DXVK、Turnip 等），不在 git 里。

动手前用下面的命令判断。命令失败，或 `git check-ignore` 命中，就不要改这个文件：

```bash
git ls-files --error-unmatch -- "app/src/main/assets/<相对路径>"
```

对未追踪文件，禁止写入、覆盖、截断、删除、重命名、移动、替换内容，也禁止用脚本、重定向或构建任务改写它们。读取和计算校验和可以。

同目录里已经被 git 追踪的文件，可以按当前任务修改。

只有用户在当次消息里明确点名要改某一个未追踪文件时，才可以改那一个文件。
