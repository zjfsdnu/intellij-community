// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.jetbrains.idea.svn.config;

import com.intellij.openapi.util.NlsContexts.DialogMessage;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static org.jetbrains.idea.svn.SvnBundle.message;

public class AmbiguousPatternsFinder {
  private final Map<String, Set<String>> urls2groups;

  private final static int ourMessageLen = 30;

  public AmbiguousPatternsFinder() {
    urls2groups = new HashMap<>();
  }

  public void acceptUrls(final String groupName, final List<String> urls) {
    for (String url : urls) {
      final Set<String> set;
      if (urls2groups.containsKey(url)) {
        set = urls2groups.get(url);
      } else {
        set = new HashSet<>();
        urls2groups.put(url, set);
      }
      set.add(groupName);
    }
  }

  public @DialogMessage @Nullable String validate() {
    StringBuilder sb = null;
    for (Map.Entry<String, Set<String>> entry : urls2groups.entrySet()) {
      if (entry.getValue().size() > 1) {
        if (sb == null) {
          sb = new StringBuilder();
        }
        else {
          if (sb.length() > ourMessageLen) {
            sb.append("...");
            break;
          }
          sb.append("; ");
        }
        StringBuilder innerBuilder = null;
        for (String groupName : entry.getValue()) {
          if (innerBuilder == null) {
            innerBuilder = new StringBuilder();
          }
          else {
            innerBuilder.append(", ");
          }
          innerBuilder.append(groupName);
        }
        sb.append(
          message("dialog.edit.http.proxies.settings.error.ambiguous.group.patterns.to.text", entry.getKey(), innerBuilder.toString()));
      }
    }

    return sb != null ? message("dialog.edit.http.proxies.settings.error.ambiguous.group.patterns.text", sb.toString()) : null;
  }
}
