# verbose for build command
# (comment the line below to enable verbose)
V = @

#################################

# source directory
SRC_DIR = src
# output directory
OUT_DIR = classes
# output doc directory
DOC_DIR = docs
# libraries directory
LIB_DIR = lib
# output jar directory
JAR_DIR = bin

# sources
SOURCE_FILES := $(shell find $(SRC_DIR) -name '*.java')
CLASSES := $(SOURCE_FILES:$(SRC_DIR)/%.java=$(OUT_DIR)/%.class)

# source packages
_PACKAGE_DIR  = $(wildcard $(SRC_DIR)/*/) 
PACKAGES = $(_PACKAGE_DIR:$(SRC_DIR)/%/=%)


# libraries
LIBS := $(wildcard $(LIB_DIR)/*.jar)

#########################################
# FLAGS


space = $(empty) $(empty)
join-with = $(subst $(space),$1,$(strip $2))

JARFLAGS = -cf
JCFLAGS = -d $(OUT_DIR)
JVDFLAGS = -sourcepath $(SRC_DIR) -d $(DOC_DIR) -subpackages $(PACKAGES)

# os check
# because Windows uses ; instead of :
ifeq ($(OS),Windows_NT)
	LIBS_CP = $(call join-with,;,$(LIBS))
	JCPFLAGS = -cp "$(OUT_DIR);$(SRC_DIR);$(LIBS_CP)"
	JTFLAGS = -cp "$(LIBS_CP);$(OUT_DIR);$(OUT_TEST_DIR)"
	RM = rmdir /s /q
else
	LIBS_CP = $(call join-with,:,$(LIBS))
	JCPFLAGS = -cp "$(OUT_DIR):$(SRC_DIR):$(LIBS_CP)"
	JTFLAGS = -cp "$(LIBS_CP):$(OUT_DIR):$(OUT_TEST_DIR)"
	RM += -r
endif
JVDFLAGS += -cp "$(LIBS_CP)"

#########################################
# COMMANDS

JC    = javac
JVM   = java
JAR   = jar
JVD   = javadoc
UNZIP = unzip -qo
CP    = cp
MKDIR = mkdir

#########################################

.PHONY: build clean run jar docs all

# default is first target found
# clean, build & run
default: build jar

$(OUT_DIR)/%.class: $(SRC_DIR)/%.java
	$(V)-$(JC) $(JCFLAGS) $(JCPFLAGS) $<

# build .java into .class
build: $(CLASSES)
	@echo Compiled source files

# Clean classes/ and docs/ (not .jar)
clean: 
	@echo Cleaning files in $(OUT_DIR)
	$(V)-$(RM) "$(OUT_DIR)"
	@echo Cleaning files in $(DOC_DIR)
	$(V)-$(RM) "$(DOC_DIR)"
	@echo Cleaning jar files in $(JAR_DIR)
	$(V)-$(RM) "$(JAR_DIR)"

# build the jar into Program.jar
jar: $(JAR_DIR)/olibe.jar $(JAR_DIR)/olibe-natives-linux.jar $(JAR_DIR)/olibe-external.jar
	@echo Created [$<]

$(JAR_DIR)/olibe.jar: build
	@echo Creating jar [$@] 
	$(V)-$(JAR) $(JARFLAGS) $@ -C $(PACKAGES:%=$(OUT_DIR) %)

$(JAR_DIR)/olibe-external.jar:
	@echo Creating jar [$@] 
	@echo JOML
	$(V)-$(UNZIP) lib/joml-1.10.5.jar -d lib_tmp/
	@echo LWGJL-ASSIMP
	$(V)-$(UNZIP) lib/lwjgl-assimp-sources.jar -d lib_tmp/
	$(V)-$(UNZIP) lib/lwjgl-assimp.jar -d lib_tmp/
	@echo LWGJL-GLFW
	$(V)-$(UNZIP) lib/lwjgl-glfw-sources.jar -d lib_tmp/
	$(V)-$(UNZIP) lib/lwjgl-glfw.jar -d lib_tmp/
	@echo LWGJL-OPENGL
	$(V)-$(UNZIP) lib/lwjgl-opengl-sources.jar -d lib_tmp/
	$(V)-$(UNZIP) lib/lwjgl-opengl.jar -d lib_tmp/
	@echo LWGJL-STB
	$(V)-$(UNZIP) lib/lwjgl-stb-sources.jar -d lib_tmp/
	$(V)-$(UNZIP) lib/lwjgl-stb.jar -d lib_tmp/
	@echo LWGJL
	$(V)-$(UNZIP) lib/lwjgl-sources.jar -d lib_tmp/
	$(V)-$(UNZIP) lib/lwjgl.jar -d lib_tmp/
	$(V)-$(JAR) $(JARFLAGS) $@ -C lib_tmp org
	$(V)-rm -rf lib_tmp

$(JAR_DIR)/olibe-natives-linux.jar:
	@echo Creating jar [$@]
	$(V)-$(UNZIP) lib/lwjgl-assimp-natives-linux.jar -d lib_tmp/
	$(V)-$(UNZIP) lib/lwjgl-glfw-natives-linux.jar -d lib_tmp/
	$(V)-$(UNZIP) lib/lwjgl-opengl-natives-linux.jar -d lib_tmp/
	$(V)-$(UNZIP) lib/lwjgl-stb-natives-linux.jar -d lib_tmp/
	$(V)-$(UNZIP) lib/lwjgl-natives-linux.jar -d lib_tmp/
	$(V)-$(JAR) $(JARFLAGS) $@ -C lib_tmp linux
	$(V)-rm -rf lib_tmp



# build the javadoc
docs:
	@echo Creating docs in [$(DOC_DIR)] 
	$(V)-$(JVD) $(JVDFLAGS)

# clean, build, build javadoc, create jar & run
all: clean build docs jar
