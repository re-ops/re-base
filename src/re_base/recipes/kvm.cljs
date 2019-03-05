(ns re-base.recipes.kvm
  "KVM virtualization support"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [cljs.core.async :refer [go <!]]
   [re-conf.resources.facts :refer [os]]
   [re-conf.resources.shell :refer (exec unless)]
   [re-conf.resources.file :refer (line file)]
   [re-conf.resources.service :refer (service)]
   [re-conf.resources.user :refer (group)]
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.output :refer (summary)]))

(defn kvm-packages
  "Installing kvm packages"
  []
  (go
    (<!
     (case (<! (os :release))
       "18.04"
       (->
        (package "qemu-kvm" "libvirt-bin" "bridge-utils" "virt-manager")
        (summary "kvm packages"))
       "18.10"
       (->
        (package "qemu-kvm" "libvirt-daemon-system" "libvirt-clients" "bridge-utils" "virt-manager")
        (summary "kvm packages"))))))

(defn kvm-base
  "Kvm base package"
  []
  (let [rules "/lib/udev/rules.d/99-kvm.rules"]
    (->
     (file rules :present)
     (line rules "KERNEL=='kvm', GROUP='kvm', MODE='0666'") ; https://bugzilla.redhat.com/show_bug.cgi?id=1479558
     (summary "kvm-base done"))))

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
   (exec "/usr/sbin/usermod" "-G" "libvirtd" "-a" (-> users :main :name))
   (summary "libvirtd-group done")))

