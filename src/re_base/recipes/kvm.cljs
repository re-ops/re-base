(ns re-base.recipes.kvm
  "KVM virtualization support"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.shell :refer (exec unless)]
   [re-conf.resources.file :refer (line)]
   [re-conf.resources.service :refer (service)]
   [re-conf.resources.user :refer (group)]
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.output :refer (summary)]))

(defn kvm-base
  "Kvm base package"
  []
  (->
   (package "qemu-kvm" "libvirt-bin" "bridge-utils" "virt-manager")
   (summary "kvm-base done")))

(defn libvirt-networking
  "Networking settings for kvm"
  []
  (let [forward "-I FORWARD -m physdev --physdev-is-bridged -j ACCEPT"]
    (->
     (exec "/bin/sed" "-i" (<< "'/^COMMIT/i ~{forward}'") "/etc/ufw/before.rules" :shell true)
     (service "ufw" :restart)
     (line "/etc/sysctl.conf" "net.bridge.bridge-nf-call-iptables = 0")
     (line "/etc/sysctl.conf" "net.bridge.bridge-nf-call-arptables = 0")
     (exec "/sbin/sysctl" "-p")
     (summary "libvirt-networking done"))))

(defn libvirtd-group
  "Setting up libvirtd group"
  [{:keys [users]}]
  (->
   (group "libvirtd" :present)
   (exec "/usr/sbin/usermod" "-G" "libvirtd" "-a" (users :main))
   (summary "libvirtd-group done")))
