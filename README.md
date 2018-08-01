# Intro

Re-base is a collection of recipes that use Re-conf for provisioning desktop and server instances

[![Build Status](https://travis-ci.org/re-ops/re-base.png)](https://travis-ci.org/re-ops/re-base)


# Usage

```bash
$ lein cljsbuild once prod
```

Provision:

```bash
$ sudo node main.js resources/{environment}.edn {profile}
```

# Copyright and license

Copyright [2018] [Ronen Narkis]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
